package com.oursurvey.repo.hashtag;

import com.oursurvey.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepo extends JpaRepository<Hashtag, Long>, HashtagRepoCustom {
}
