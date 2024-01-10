package com.vizen.controller;

import com.vizen.entity.ForecastDashBoard;
import com.vizen.request.dto.CreateUpdateForecastDashBoardDto;
import com.vizen.service.ForecastDashBoardService;
import com.vizen.service.UserService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "ForecastDashBoards")
@RestController
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/forecast_dashboard")
public class ForecastDashBoardController {

    private ForecastDashBoardService forecastDashBoardService;

    private UserService userService;

    @Autowired
    public ForecastDashBoardController(ForecastDashBoardService forecastDashBoardService, UserService userService) {
        this.forecastDashBoardService = forecastDashBoardService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ForecastDashBoard>> getList(
            @RequestParam(name = "orgId", required = false) final String orgId
    )  {
        return ResponseEntity.ok(forecastDashBoardService.getByOrgId(userService.getLoggedInUser(), orgId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ForecastDashBoard> get(@PathVariable String id) {
        return ResponseEntity.ok(forecastDashBoardService.getById(id));
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ForecastDashBoard> create(@Valid @RequestBody CreateUpdateForecastDashBoardDto createUpdateForecastDashBoardDto) {
        return ResponseEntity.ok(forecastDashBoardService.createOrUpdate(Optional.empty(), createUpdateForecastDashBoardDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ForecastDashBoard> update(@PathVariable String id,
                                                           @Valid @RequestBody CreateUpdateForecastDashBoardDto createUpdateForecastDashBoardDto) {
        return ResponseEntity.ok(forecastDashBoardService.createOrUpdate(Optional.of(id), createUpdateForecastDashBoardDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        forecastDashBoardService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/quick_sight_url")
    public ResponseEntity<String> generateUrl(@PathVariable String id)
     {
        return ResponseEntity.ok(forecastDashBoardService.regenerateDashBoardUrl(id).getUrl());
    }

}
