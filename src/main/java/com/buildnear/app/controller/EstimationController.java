package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.EstimationDto;
import com.buildnear.app.service.EstimationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estimations")
public class EstimationController {

    private final EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @PostMapping("/construction")
    public ResponseEntity<ApiResponse<EstimationDto>> calculateConstructionEstimate(@RequestBody EstimationDto input) {
        EstimationDto estimate = estimationService.estimateHouseConstruction(input);
        return ResponseEntity.ok(ApiResponse.ok("House construction cost estimated successfully", estimate));
    }

    @PostMapping("/interior")
    public ResponseEntity<ApiResponse<EstimationDto>> calculateInteriorEstimate(@RequestBody EstimationDto input) {
        EstimationDto estimate = estimationService.estimateInteriorDesign(input);
        return ResponseEntity.ok(ApiResponse.ok("Interior design cost estimated successfully", estimate));
    }
}
