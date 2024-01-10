package com.vizen.controller;

import com.vizen.entity.ForecastDashBoard;
import com.vizen.service.ForecastDashBoardService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Forecasts")
@RestController
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/forecast")
public class ForecastListController {

    private ForecastDashBoardService forecastDashBoardService;

    @Autowired
    public ForecastListController(ForecastDashBoardService forecastDashBoardService) {
        this.forecastDashBoardService = forecastDashBoardService;
    }

    @GetMapping("/urlList")
    public ResponseEntity<List<ForecastDashBoard>> getList(
            @RequestParam(name = "orgId", required = false) String orgId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(forecastDashBoardService.getForecastUrls(authentication.getName(), orgId));
    }

    @GetMapping("/url")
    public ResponseEntity<ForecastDashBoard> generateUrl(
            @RequestParam(name = "id", required = true) String id
    ) {
        return ResponseEntity.ok(forecastDashBoardService.regenerateDashBoardUrl(id));
    }


}
