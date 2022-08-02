package com.oursurvey.service.survey;

import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.repo.survey.SurveyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepo repo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(SurveyDto.Create dto) {
        return null;
    }
}
