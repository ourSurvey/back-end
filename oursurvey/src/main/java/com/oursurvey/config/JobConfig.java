package com.oursurvey.config;

import com.oursurvey.service.experience.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class JobConfig {
    private final ExperienceService experienceService;

    @Scheduled(fixedRate = 60 * 60 * 24, timeUnit = TimeUnit.SECONDS)
    public void recentAppVersion() {
        experienceService.findSumAndPromoting();
    }
}
