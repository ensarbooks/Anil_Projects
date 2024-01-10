package com.vizen.service;

import com.vizen.entity.ForecastDashBoard;
import com.vizen.entity.User;
import com.vizen.repository.ForecastDashBoardRepository;
import com.vizen.repository.OrganizationRepository;
import com.vizen.repository.UserRepository;
import com.vizen.request.dto.CreateUpdateForecastDashBoardDto;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Log4j2
public class ForecastDashBoardService {

    private UserRepository userRepository;

    private OrganizationRepository organizationRepository;

    private ForecastDashBoardRepository forecastDashBoardRepository;

    private QuickSightURLGenerator quickSightURLGenerator;

    @Autowired
    public ForecastDashBoardService(OrganizationRepository organizationRepository,
                                    ForecastDashBoardRepository forecastDashBoardRepository,
                                    UserRepository userRepository,
                                    QuickSightURLGenerator quickSightURLGenerator) {

        this.organizationRepository = organizationRepository;
        this.forecastDashBoardRepository = forecastDashBoardRepository;
        this.userRepository = userRepository;
        this.quickSightURLGenerator = quickSightURLGenerator;
    }

    public List<ForecastDashBoard> getByOrgId(User loggedInUser, String orgId) {

        if (loggedInUser.getRole().equals(User.Role.ROLE_SUPER_ADMIN)) {
            if (orgId != null) {
                return forecastDashBoardRepository.findByOrganizationIdOrderByCreatedDateTimeDesc(orgId);
            }
            return forecastDashBoardRepository.findAll();
        }

        return forecastDashBoardRepository.findByOrganizationIdOrderByCreatedDateTimeDesc(loggedInUser.getOrganization().getId());
    }


    public ForecastDashBoard getById(String id) {
        Optional<ForecastDashBoard> forecastDashBoard = forecastDashBoardRepository.findById(id);
        if (!forecastDashBoard.isPresent())
            throw new RuntimeException("ForecastDashBoard with id " + id + " not found");
        return forecastDashBoard.get();
    }

    public void deleteById(String dashBoardId) {
        getById(dashBoardId);
        forecastDashBoardRepository.deleteById(dashBoardId);
    }

    public ForecastDashBoard createOrUpdate(Optional<String> dashBoardId, CreateUpdateForecastDashBoardDto createUpdateForecastDashBoardDto) {
        ForecastDashBoard forecastDashBoard;
        if (dashBoardId.isPresent()) {
            forecastDashBoard = getById(dashBoardId.get());
        } else {
            forecastDashBoard = new ForecastDashBoard();
        }
        forecastDashBoard.setDashBoardId(createUpdateForecastDashBoardDto.getDashBoardId());
        forecastDashBoard.setOrganization(organizationRepository.findById(createUpdateForecastDashBoardDto.getOrganizationId()).get());
        forecastDashBoard.setDescription(createUpdateForecastDashBoardDto.getDescription());
        forecastDashBoard.setName(createUpdateForecastDashBoardDto.getName());
        return forecastDashBoardRepository.save(forecastDashBoard);
    }


    public List<ForecastDashBoard> getForecastUrls(String email, String orgId) {
        User user = userRepository.findByEmail(email);
        String forecastOrgId = (user.getRole() == User.Role.ROLE_SUPER_ADMIN && orgId != null) ? orgId : user.getOrganization().getId();
        List<ForecastDashBoard> dashBoardList = forecastDashBoardRepository.findByOrganizationIdOrderByCreatedDateTimeDesc(forecastOrgId);
        if(!dashBoardList.isEmpty()) {
            refreshDashBoardUrl(dashBoardList.get(0));
        }
        return dashBoardList;
    }

    public ForecastDashBoard regenerateDashBoardUrl(String id) {
        return refreshDashBoardUrl(getById(id));
    }

    private ForecastDashBoard refreshDashBoardUrl(ForecastDashBoard forecastDashBoard) {
        if(StringUtils.hasText(forecastDashBoard.getDashBoardId())) {
            log.info("#### Generating URL for dashboard Id:" + forecastDashBoard.getDashBoardId());
            String url = quickSightURLGenerator.getQuicksightEmbedUrl(forecastDashBoard.getDashBoardId());
            forecastDashBoard.setUrl(url);
        }
        return forecastDashBoard;
    }
}
