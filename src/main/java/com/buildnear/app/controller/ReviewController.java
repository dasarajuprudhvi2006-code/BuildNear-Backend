package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.dto.ReviewDto;
import com.buildnear.app.security.UserPrincipal;
import com.buildnear.app.service.ProfessionalService;
import com.buildnear.app.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProfessionalService professionalService;

    public ReviewController(ReviewService reviewService, ProfessionalService professionalService) {
        this.reviewService = reviewService;
        this.professionalService = professionalService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ReviewDto dto) {

        ReviewDto created = reviewService.createReview(userPrincipal.getId(), dto);
        return ResponseEntity.ok(ApiResponse.ok("Review submitted successfully", created));
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsForProfessional(@PathVariable Long professionalId) {
        List<ReviewDto> reviews = reviewService.getReviewsForProfessional(professionalId);
        return ResponseEntity.ok(ApiResponse.ok("Reviews retrieved", reviews));
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<ApiResponse<ReviewDto>> respondToReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        ProfessionalProfileDto pro = professionalService.getProfileByUserId(userPrincipal.getId());
        String responseText = body.get("response");
        ReviewDto updated = reviewService.respondToReview(id, pro.getId(), responseText);
        return ResponseEntity.ok(ApiResponse.ok("Response posted", updated));
    }
}
