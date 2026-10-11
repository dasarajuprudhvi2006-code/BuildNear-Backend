package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.security.UserPrincipal;
import com.buildnear.app.service.DashboardService;
import com.buildnear.app.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ProfessionalService professionalService;

    public DashboardController(DashboardService dashboardService, ProfessionalService professionalService) {
        this.dashboardService = dashboardService;
        this.professionalService = professionalService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        boolean isProfessional = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSIONAL"));

        Map<String, Object> stats;
        if (isProfessional) {
            ProfessionalProfileDto pro = professionalService.getProfileByUserId(userPrincipal.getId());
            stats = dashboardService.getProfessionalStats(pro.getId());
        } else {
            stats = dashboardService.getCustomerStats(userPrincipal.getId());
        }

        return ResponseEntity.ok(ApiResponse.ok("User dashboard stats", stats));
    }
}
