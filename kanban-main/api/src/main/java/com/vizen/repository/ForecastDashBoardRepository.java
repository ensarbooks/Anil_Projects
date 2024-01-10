package com.vizen.repository;

import com.vizen.entity.ForecastDashBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastDashBoardRepository extends JpaRepository<ForecastDashBoard, String> {
    List<ForecastDashBoard> findByOrganizationIdOrderByCreatedDateTimeDesc(String orgId);
}
