package com.oursurvey.service.topsurvey;

import com.oursurvey.dto.repo.SurveyDto;
import com.oursurvey.repo.topsurvey.TopSurveyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TopSurveyServiceImpl implements TopSurveyService {
    private final TopSurveyRepo repo;
}
