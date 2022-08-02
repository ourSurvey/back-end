package com.oursurvey.repo.questionitem;

import com.oursurvey.entity.QuestionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionItemRepo extends JpaRepository<QuestionItem, Long>, QuestionItemRepoCustom {
}
