package com.vizen.repository;

import com.vizen.entity.UserPasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordResetRequestRepository extends JpaRepository<UserPasswordResetRequest, String> {
}
