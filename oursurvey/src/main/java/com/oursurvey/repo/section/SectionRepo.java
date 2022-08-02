package com.oursurvey.repo.section;

import com.oursurvey.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepo extends JpaRepository<Section, Long>, SectionRepoCustom{
}
