package com.oursurvey.service.point;

import com.oursurvey.dto.repo.PointDto;

public interface PointService {
    Integer create(PointDto.Create dto); // 적립된 포인트 return
}
