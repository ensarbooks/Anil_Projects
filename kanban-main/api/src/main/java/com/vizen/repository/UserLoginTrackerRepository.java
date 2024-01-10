package com.vizen.repository;

import com.vizen.entity.UserLoginTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginTrackerRepository extends JpaRepository<UserLoginTracker, String> {

}
