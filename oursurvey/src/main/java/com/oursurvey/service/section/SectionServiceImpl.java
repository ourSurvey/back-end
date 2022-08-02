package com.oursurvey.service.section;

import com.oursurvey.dto.repo.SectionDto;
import com.oursurvey.repo.section.SectionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepo repo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(SectionDto.Create dto) {
        return null;
    }
}
