package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.dto.ServiceRequestDto;
import com.buildnear.app.security.UserPrincipal;
import com.buildnear.app.service.ProfessionalService;
import com.buildnear.app.service.ServiceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ServiceRequestController {

    private final ServiceRequestService requestService;
    private final ProfessionalService professionalService;

    public ServiceRequestController(ServiceRequestService requestService, ProfessionalService professionalService) {
        this.requestService = requestService;
        this.professionalService = professionalService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceRequestDto>> createRequest(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody ServiceRequestDto dto) {

        ServiceRequestDto created = requestService.createRequest(userPrincipal.getId(), dto);
        return ResponseEntity.ok(ApiResponse.ok("Service request posted successfully", created));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ServiceRequestDto>>> getMyRequests(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<ServiceRequestDto> requests = requestService.getCustomerRequests(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("My service requests", requests));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<ServiceRequestDto>>> getAvailableRequestsForPro(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ProfessionalProfileDto proProfile = professionalService.getProfileByUserId(userPrincipal.getId());
        List<ServiceRequestDto> requests = requestService.getRequestsAvailableForProfessional(proProfile.getId());
        return ResponseEntity.ok(ApiResponse.ok("Available service requests for quotation", requests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> getRequestById(@PathVariable Long id) {
        ServiceRequestDto dto = requestService.getRequestById(id);
        return ResponseEntity.ok(ApiResponse.ok("Service request details", dto));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> cancelRequest(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {

        ServiceRequestDto cancelled = requestService.cancelRequest(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Service request cancelled", cancelled));
    }
}
