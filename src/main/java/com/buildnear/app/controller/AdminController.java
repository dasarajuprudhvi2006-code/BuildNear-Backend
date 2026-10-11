package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.BookingDto;
import com.buildnear.app.dto.DashboardStatsDto;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.dto.ReviewDto;
import com.buildnear.app.entity.User;
import com.buildnear.app.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final ProfessionalService professionalService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    public AdminController(DashboardService dashboardService,
                           UserService userService,
                           ProfessionalService professionalService,
                           BookingService bookingService,
                           ReviewService reviewService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
        this.professionalService = professionalService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getStats() {
        DashboardStatsDto stats = dashboardService.getAdminStats();
        return ResponseEntity.ok(ApiResponse.ok("Platform administrative stats", stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("All users retrieved", users));
    }

    @PatchMapping("/users/{id}/toggle-status")
    public ResponseEntity<ApiResponse<User>> toggleUserStatus(@PathVariable Long id) {
        User updated = userService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.ok("User status updated", updated));
    }

    @GetMapping("/professionals")
    public ResponseEntity<ApiResponse<List<ProfessionalProfileDto>>> getAllProfessionals() {
        List<ProfessionalProfileDto> pros = professionalService.searchProfessionals(
                null, null, null, null, null, null, null, null, null, null, "rating_desc"
        );
        return ResponseEntity.ok(ApiResponse.ok("All professionals retrieved", pros));
    }

    @PatchMapping("/professionals/{id}/verify")
    public ResponseEntity<ApiResponse<ProfessionalProfileDto>> verifyProfessional(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {

        boolean verified = body.getOrDefault("verified", true);
        ProfessionalProfileDto updated = professionalService.setVerificationStatus(id, verified);
        return ResponseEntity.ok(ApiResponse.ok("Professional verification status updated", updated));
    }

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getAllBookings() {
        List<BookingDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.ok("All platform bookings", bookings));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.ok("All customer reviews", reviews));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.ok("Review removed by admin", null));
    }
}
