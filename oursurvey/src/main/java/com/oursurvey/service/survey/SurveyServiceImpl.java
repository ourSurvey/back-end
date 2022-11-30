package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.*;
import com.oursurvey.entity.*;
import com.oursurvey.exception.AuthFailException;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.exception.PointLackException;
import com.oursurvey.repo.experience.ExperienceRepo;
import com.oursurvey.repo.hashtag.HashtagRepo;
import com.oursurvey.repo.hashtagsurvey.HashtagSurveyRepo;
import com.oursurvey.repo.point.PointRepo;
import com.oursurvey.repo.question.QuestionRepo;
import com.oursurvey.repo.questionitem.QuestionItemRepo;
import com.oursurvey.repo.section.SectionRepo;
import com.oursurvey.repo.survey.SurveyRepo;
import com.oursurvey.repo.user.UserRepo;
import com.oursurvey.service.experience.ExperienceService;
import com.oursurvey.service.pkmanager.PkManagerService;
import com.oursurvey.service.point.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final ExperienceRepo experienceRepo;
    private final HashtagRepo hashtagRepo;
    private final HashtagSurveyRepo hashtagSurveyRepo;

    private final PkManagerService pkManager;
    private final PointService pointService;
    private final ExperienceService experienceService;
    private final UserRepo userRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<SurveyDto.Detail> findById(String id) {
        Optional<Survey> opt = repo.getFromId(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        Survey survey = opt.get();
        survey.addViewCnt();

        List<Section> sectionList = sectionRepo.getBySurveyId(survey.getId());
        List<SectionDto.Detail> sectionDetailDtoList = sectionList.stream().map(section -> {
            List<Question> questionList = questionRepo.getBySectionId(section.getId());

            List<QuestionDto.Detail> questionDetailDtoList = questionList.stream().map(question -> {
                List<QuestionItem> questionItemList = questionItemRepo.getByQuestionId(question.getId());

                List<QuestionItemDto.Detail> questionItemDetailDtoList = questionItemList.stream().map(questionItem ->
                        QuestionItemDto.Detail.builder()
                                .id(questionItem.getId())
                                .content(questionItem.getContent())
                                .oder(questionItem.getOder())
                                .nextSection(questionItem.getNextSection())
                                .build()).toList();

                return QuestionDto.Detail.builder()
                        .id(question.getId())
                        .ask(question.getAsk())
                        .descrip(question.getDescrip())
                        .multiFl(question.getMultiFl())
                        .essFl(question.getEssFl())
                        .dupFl(question.getDuplFl())
                        .randomShowFl(question.getRandomShowFl())
                        .nextFl(question.getNextFl())
                        .oder(question.getOder())
                        .questionItemList(questionItemDetailDtoList)
                        .build();
            }).toList();

            return SectionDto.Detail.builder()
                    .id(section.getId())
                    .title(section.getTitle())
                    .content(section.getContent())
                    .nextSection(section.getNextSection())
                    .questionList(questionDetailDtoList)
                    .build();
        }).toList();

        return Optional.of(SurveyDto.Detail.builder()
                .id(survey.getId())
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
    public String create(SurveyDto.Create dto) {
        if (pointService.findSumByUserId(dto.getUserId()) < -(Point.CREATE_SURVEY_VALUE)) {
            throw new PointLackException();
        }

        // 기존꺼 삭제
        if (StringUtils.hasText(dto.getId())) {
            Optional<Survey> surveyOpt = repo.getFromId(dto.getId());
            if (surveyOpt.isEmpty()) {
                throw new ObjectNotFoundException("no survey");
            }

            Survey survey = surveyOpt.get();
            if (!survey.getUser().getId().equals(dto.getUserId())) {
                throw new AuthFailException("it`s not your survey");
            }

            // FIXME. 파일추가 되면 DELETE 로직 수정해야함
            repo.delete(Survey.builder().id(dto.getId()).build());
        }

        Survey saveSurvey = repo.save(Survey.builder()
                .id(pkManager.createOrUpdateByTableName(Survey.NAME))
                .user(User.builder().id(dto.getUserId()).build())
                .subject(dto.getSubject())
                .content(dto.getContent())
                .minute(dto.getMinute())
                .startDate(LocalDate.parse(dto.getStartDate()))
                .endDate(LocalDate.parse(dto.getEndDate()))
                .openFl(dto.getOpenFl())
                .tempFl(dto.getTempFl())
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
                    .id(pkManager.createOrUpdateByTableName(Section.NAME))
                    .survey(Survey.builder().id(saveSurvey.getId()).build())
                    .title(section.getTitle())
                    .content(section.getContent())
                    .nextSection(section.getNextSection())
                    .build());

            List<QuestionDto.Create> questionList = section.getQuestionList();
            questionList.forEach(question -> {
                Question saveQuestion = questionRepo.save(Question.builder()
                        .id(pkManager.createOrUpdateByTableName(Question.NAME))
                        .section(Section.builder().id(saveSection.getId()).build())
                        .ask(question.getAsk())
                        .descrip(question.getDescrip())
                        .oder(question.getOder())
                        .multiFl(question.getMultiFl())
                        .duplFl(question.getDupFl())
                        .essFl(question.getEssFl())
                        .randomShowFl(question.getRandomShowFl())
                        .nextFl(question.getNextFl())
                        .build());

                List<QuestionItemDto.Create> questionItemList = question.getQuestionItemList();
                questionItemList.forEach(item -> {
                    questionItemRepo.save(QuestionItem.builder()
                            .id(pkManager.createOrUpdateByTableName(QuestionItem.NAME))
                            .question(Question.builder().id(saveQuestion.getId()).build())
                            .content(item.getContent())
                            .oder(item.getOder())
                            .nextSection(item.getNextSection())
                            .build());
                });
            });
        });

        if (dto.getTempFl().equals(0)) {
            // save point
            pointRepo.save(Point.builder()
                    .user(User.builder().id(dto.getUserId()).build())
                    .value(Point.CREATE_SURVEY_VALUE)
                    .reason(Point.CREATE_SURVEY_REASON)
                    .tablePk(saveSurvey.getId())
                    .tableName(Survey.NAME)
                    .build());

            // save experience
            experienceRepo.save(Experience.builder()
                    .user(User.builder().id(dto.getUserId()).build())
                    .value(Experience.CREATE_SURVEY_VALUE)
                    .reason(Experience.CREATE_SURVEY_REASON)
                    .tablePk(saveSurvey.getId())
                    .tableName(Survey.NAME)
                    .build());
        } else {
            // 임시 저장이라면 + 20개가 넘었다면 맨 마지막 삭제해야함
            List<Survey> tempList = repo.getTempByUserId(dto.getUserId());
            if (tempList.size() > 20) {
                Survey lastSurvey = tempList.get(tempList.size() - 1);
                repo.delete(Survey.builder().id(lastSurvey.getId()).build());
            }
        }

        return saveSurvey.getId();
    }

    @Override
    public Page<SurveyDto.Lizt> find(Pageable pageable, String condition, Object obj) {
        return repo.get(pageable, condition, obj);
    }

    @Override
    public List<SurveyDto.MyList> findByUserId(Long userId) {
        return repo.getByUserId(userId);
    }

    @Override
    public List<SurveyDto.MyListTemp> findTempByUserId(Long userId) {
        return repo.getTempByUserId(userId).stream().map(e -> SurveyDto.MyListTemp.builder()
                .id(e.getId())
                .subject(e.getSubject())
                .createdDt(e.getCreateDt())
                .build()).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pull(Long userId, String surveyId) {
        Optional<Survey> opt = repo.getFromId(surveyId);
        if (opt.isEmpty()) {
            throw new ObjectNotFoundException("no survey");
        }
        Survey survey = opt.get();

        if (!survey.getUser().getId().equals(userId)) {
            throw new AuthFailException("it`s not your survey");
        }

        if (pointService.findSumByUserId(userId) < -(Point.PULLING_SURVEY_VALUE)) {
            throw new PointLackException();
        }

        survey.pullUp();

        pointRepo.save(Point.builder()
                .user(User.builder().id(userId).build())
                .value(Point.PULLING_SURVEY_VALUE)
                .reason(Point.PULLING_SURVEY_REASON)
                .tablePk(survey.getId())
                .tableName("survey")
                .build());

        experienceRepo.save(Experience.builder()
                .user(User.builder().id(userId).build())
                .value(Experience.PULLING_SURVEY_VALUE)
                .reason(Experience.PULLING_SURVEY_REASON)
                .tablePk(survey.getId())
                .tableName("survey")
                .build());
    }
}
