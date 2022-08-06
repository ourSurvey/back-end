package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.QuestionDto;
import com.oursurvey.dto.repo.QuestionItemDto;
import com.oursurvey.dto.repo.SectionDto;
import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.entity.*;
import com.oursurvey.repo.question.QuestionRepo;
import com.oursurvey.repo.questionitem.QuestionItemRepo;
import com.oursurvey.repo.section.SectionRepo;
import com.oursurvey.repo.survey.SurveyRepo;
import com.oursurvey.service.section.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepo repo;
    private final SectionRepo sectionRepo;
    private final QuestionRepo questionRepo;
    private final QuestionItemRepo questionItemRepo;

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

        List<SectionDto.Create> sectionList = dto.getSectionList();
        sectionList.forEach(section -> {
            Section saveSection = sectionRepo.save(Section.builder()
                    .survey(Survey.builder().id(saveSurvey.getId()).build())
                    .title(section.getTitle())
                    .nextSection(section.getNextSection())
                    .build());

            List<QuestionDto.Create> questionList = section.getQuestionList();
            questionList.forEach(question -> {
                Question saveQuestion = questionRepo.save(Question.builder()
                        .section(Section.builder().id(saveSection.getId()).build())
                        .ask(question.getAsk())
                        .explain(question.getExplain())
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
                            .nextSection(item.getNextSection())
                            .build());
                });
            });
        });

        return saveSurvey.getId();
    }

    @Override
    public Page<SurveyDto.Lizt> find(Pageable pageable) {
        return repo.get(pageable);
    }
}
