package com.oursurvey.service.reply;

import com.oursurvey.dto.repo.AnswerDto;
import com.oursurvey.dto.repo.ReplyDto;
import com.oursurvey.entity.*;
import com.oursurvey.exception.AlreadyReplySurveyException;
import com.oursurvey.exception.ObjectNotFoundException;
import com.oursurvey.repo.answer.AnswerRepo;
import com.oursurvey.repo.experience.ExperienceRepo;
import com.oursurvey.repo.point.PointRepo;
import com.oursurvey.repo.question.QuestionRepo;
import com.oursurvey.repo.reply.ReplyRepo;
import com.oursurvey.repo.survey.SurveyRepo;
import com.oursurvey.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepo repo;
    private final AnswerRepo answerRepo;
    private final SurveyRepo surveyRepo;
    private final QuestionRepo questionRepo;
    private final PointRepo pointRepo;
    private final ExperienceRepo experienceRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(ReplyDto.Create dto) {
        Optional<Survey> surveyOpt = surveyRepo.getFromId(dto.getSurveyId());
        if (surveyOpt.isEmpty()) {
            throw new ObjectNotFoundException("no survey");
        }

        // 중복 참여 불가
        if (dto.getUserId() != null) {
            Optional<Reply> optReply = repo.getBySurveyIdUserId(dto.getSurveyId(), dto.getUserId());
            if (optReply.isPresent()) {
                throw new AlreadyReplySurveyException("already reply survey");
            }
        }

        // NOTE. 절대 User.builder().id(null).build() 하면 안됨 -> null로 객체를 넣으면 안됨
        // NOTE. object references an unsaved transient instance 에러가 발생함
        // NOTE. User.builder().id(null).build() -> id가 null인 컬럼을 당연히 찾기 못하기 때문임
        Reply.ReplyBuilder replyBuilder = Reply.builder().survey(Survey.builder().id(dto.getSurveyId()).build());
        if (dto.getUserId() != null) {
            replyBuilder.user(User.builder().id(dto.getUserId()).build());
        }

        // save reply
        Reply saveReply = repo.save(replyBuilder.build());

        dto.getAnswerList().forEach(e -> {
            Optional<Question> questionOpt = questionRepo.getFromId(e.getQuestionId());
            if (questionOpt.isEmpty()) {
                throw new ObjectNotFoundException("no question");
            }

            // save answer
            answerRepo.save(Answer.builder()
                .reply(saveReply)
                .question(Question.builder().id(e.getQuestionId()).build())
                .response(StringUtils.hasText(e.getValue()) ? e.getValue() : null)
                .build());
        });

        // save point & experience
        if (dto.getUserId() != null) {
            pointRepo.save(Point.builder()
                    .user(User.builder().id(dto.getUserId()).build())
                    .value(Point.REPLY_SURVEY_VALUE)
                    .reason(Point.REPLY_SURVEY_REASON)
                    .tablePk(saveReply.getId())
                    .tableName("reply")
                    .build());

            experienceRepo.save(Experience.builder()
                    .user(User.builder().id(dto.getUserId()).build())
                    .value(Experience.REPLY_SURVEY_VALUE)
                    .reason(Experience.REPLY_SURVEY_REASON)
                    .tablePk(saveReply.getId())
                    .tableName("reply")
                    .build());
        }

        return saveReply.getId();
    }
}
