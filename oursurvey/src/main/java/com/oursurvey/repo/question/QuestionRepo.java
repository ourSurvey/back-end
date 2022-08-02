package com.oursurvey.repo.question;

import com.oursurvey.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long>, QuestionRepoCustom {
}
