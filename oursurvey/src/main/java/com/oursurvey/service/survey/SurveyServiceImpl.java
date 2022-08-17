package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.*;
import com.oursurvey.entity.*;
import com.oursurvey.repo.hashtag.HashtagRepo;
import com.oursurvey.repo.hashtagsurvey.HashtagSurveyRepo;
import com.oursurvey.repo.point.PointRepo;
import com.oursurvey.repo.question.QuestionRepo;
import com.oursurvey.repo.questionitem.QuestionItemRepo;
import com.oursurvey.repo.section.SectionRepo;
import com.oursurvey.repo.survey.SurveyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepo repo;
    private final SectionRepo sectionRepo;
    private final QuestionRepo questionRepo;
    private final QuestionItemRepo questionItemRepo;
    private final PointRepo pointRepo;
    private final HashtagRepo hashtagRepo;
    private final HashtagSurveyRepo hashtagSurveyRepo;

    @Override
    public Optional<SurveyDto.Detail> findById(Long id) {
        Optional<Survey> opt = repo.getFromId(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        Survey survey = opt.get();
        List<Section> sectionList = sectionRepo.getBySurveyId(survey.getId());
        List<SectionDto.Detail> sectionDetailDtoList = sectionList.stream().map(section -> {
            List<Question> questionList = questionRepo.getBySectionId(section.getId());

            List<QuestionDto.Detail> questionDetailDtoList = questionList.stream().map(question -> {
                List<QuestionItem> questionItemList = questionItemRepo.getByQuestionId(question.getId());

                List<QuestionItemDto.Detail> questionItemDetailDtoList = questionItemList.stream().map(questionItem ->
                        QuestionItemDto.Detail.builder()
                                .content(questionItem.getContent())
                                .oder(questionItem.getOder())
                                .nextSection(questionItem.getNextSection())
                                .build()).toList();

                return QuestionDto.Detail.builder()
                        .ask(question.getAsk())
                        .descrip(question.getDescrip())
                        .multiFl(question.getMultiFl())
                        .essFl(question.getEssFl())
                        .dupFl(question.getDuplFl())
                        .oder(question.getOder())
                        .questionItemList(questionItemDetailDtoList)
                        .build();
            }).toList();

            return SectionDto.Detail.builder()
                    .title(section.getTitle())
                    .content(section.getContent())
                    .nextSection(section.getNextSection())
                    .questionList(questionDetailDtoList)
                    .build();
        }).toList();

        return Optional.of(SurveyDto.Detail.builder()
                .nickname(survey.getUser().getNickname())
                .subject(survey.getSubject())
                .content(survey.getContent())
                .openFl(survey.getOpenFl())
                .minute(survey.getMinute())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .createdDt(survey.getCreateDt())
                .hashtagList(hashtagSurveyRepo.getBySurveyId(survey.getId()).stream().map(HashtagDto.Base::getValue).toList())
                .sectionList(sectionDetailDtoList)
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(SurveyDto.Create dto) {
        Survey saveSurvey = repo.save(Survey.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .subject(dto.getSubject())
                .content(dto.getContent())
                .minute(dto.getMinute())
                .startDate(LocalDate.parse(dto.getStartDate()))
                .endDate(LocalDate.parse(dto.getEndDate()))
                .openFl(dto.getOpenFl())
                .closingComment(dto.getClosingComment())
                .build());

        List<String> hashtagList = dto.getHashtagList();
        hashtagList.forEach(val -> {
            Optional<Hashtag> opt = hashtagRepo.getByValue(val);
            if (opt.isEmpty()) {
                // save hashtag
                Hashtag save = hashtagRepo.save(Hashtag.builder().value(val).build());
                opt = Optional.of(save);
            }

            // save hashtagSurvey
            hashtagSurveyRepo.save(HashtagSurvey.builder()
                    .hashtag(opt.get())
                    .survey(saveSurvey)
                    .build());
        });

        List<SectionDto.Create> sectionList = dto.getSectionList();
        sectionList.forEach(section -> {
            Section saveSection = sectionRepo.save(Section.builder()
                    .survey(Survey.builder().id(saveSurvey.getId()).build())
                    .title(section.getTitle())
                    .content(section.getContent())
                    .nextSection(section.getNextSection())
                    .build());

            List<QuestionDto.Create> questionList = section.getQuestionList();
            questionList.forEach(question -> {
                Question saveQuestion = questionRepo.save(Question.builder()
                        .section(Section.builder().id(saveSection.getId()).build())
                        .ask(question.getAsk())
                        .descrip(question.getDescrip())
                        .oder(question.getOder())
                        .multiFl(question.getMultiFl())
                        .duplFl(question.getDupFl())
                        .essFl(question.getEssFl())
                        .build());

                List<QuestionItemDto.Create> questionItemList = question.getQuestionItemList();
                questionItemList.forEach(item -> {
                    questionItemRepo.save(QuestionItem.builder()
                            .question(Question.builder().id(saveQuestion.getId()).build())
                            .content(item.getContent())
                            .oder(item.getOder())
                            .nextSection(item.getNextSection())
                            .build());
                });
            });
        });

        // save point
        pointRepo.save(Point.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .value(Point.CREATE_SURVEY_VALUE)
                .reason(Point.CREATE_SURVEY_REASON)
                .tablePk(saveSurvey.getId())
                .tableName("survey")
                .build());

        return saveSurvey.getId();
    }

    @Override
    public Page<SurveyDto.Lizt> find(Pageable pageable) {
        return repo.get(pageable);
    }
}
