package com.vizen.service;

import com.vizen.entity.Organization;
import com.vizen.repository.OrganizationRepository;
import com.vizen.request.dto.CreateUpdateOrgDto;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizationService {

    private OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {

        this.organizationRepository = organizationRepository;
    }

    public Organization getOrganizationById(String id) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);

        if (!organizationOptional.isPresent())
            throw new RuntimeException("Organization with " + id + " not found.");
        return organizationOptional.get();
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization createOrUpdateOrganization(Optional<String> orgId, CreateUpdateOrgDto createUpdateOrgDto) {
        Organization organization;
        if (orgId.isPresent()) {
            organization = organizationRepository.getById(orgId.get());
            if (organization == null)
                throw new RuntimeException("Organization with id " + orgId.get() + " not found");
        } else {
            organization = new Organization();
            if (organizationRepository.existsByName(createUpdateOrgDto.getName()))
                throw new RuntimeException("Organization with name " + createUpdateOrgDto.getName() + " already exists.");
        }

        organization.setName(createUpdateOrgDto.getName());
        organization.setDescription(createUpdateOrgDto.getDescription());
        organization.setDomain(createUpdateOrgDto.getDomain());
        organization.setLogoImgSrc(createUpdateOrgDto.getLogoImgSrc());

        organization = organizationRepository.save(organization);
        return organization;
    }

    public void enableOrDisableOrgs(List<String> orgIdList, final boolean disabled) {
        orgIdList.forEach(id -> {
            organizationRepository.getById(id).setDisabled(disabled);
        });
    }
}
