package com.oursurvey.repo.point;

import com.oursurvey.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepo extends JpaRepository<Point, Long>, PointRepoCustom {
}
