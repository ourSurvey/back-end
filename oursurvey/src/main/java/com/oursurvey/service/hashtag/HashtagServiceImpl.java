package com.oursurvey.service.hashtag;

import com.oursurvey.dto.repo.HashtagDto;
import com.oursurvey.entity.Hashtag;
import com.oursurvey.repo.hashtag.HashtagRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepo repo;

    @Override
    public List<String> findListByValue(String value) {
        return repo.getListByValue(value).stream().map(Hashtag::getValue).toList();
    }
}
