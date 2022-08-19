package com.oursurvey.repo.answer;

import com.oursurvey.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepo extends JpaRepository<Answer, Long>, AnswerRepoCustom {
}
