package com.vizen.repository;

import com.vizen.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
    Organization findByName(String name);

    boolean existsByName(String name);
}
