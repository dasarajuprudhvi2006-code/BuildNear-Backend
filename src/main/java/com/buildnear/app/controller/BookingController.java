package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.BookingDto;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.security.UserPrincipal;
import com.buildnear.app.service.BookingService;
import com.buildnear.app.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final ProfessionalService professionalService;

    public BookingController(BookingService bookingService, ProfessionalService professionalService) {
        this.bookingService = bookingService;
        this.professionalService = professionalService;
    }

    @PostMapping("/direct/{professionalId}")
    public ResponseEntity<ApiResponse<BookingDto>> createDirectBooking(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long professionalId,
            @RequestBody BookingDto dto) {

        BookingDto created = bookingService.createDirectBooking(userPrincipal.getId(), professionalId, dto);
        return ResponseEntity.ok(ApiResponse.ok("Booking requested successfully", created));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getMyBookings(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<BookingDto> bookings;
        boolean isProfessional = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSIONAL"));

        if (isProfessional) {
            ProfessionalProfileDto pro = professionalService.getProfileByUserId(userPrincipal.getId());
            bookings = bookingService.getProfessionalBookings(pro.getId());
        } else {
            bookings = bookingService.getCustomerBookings(userPrincipal.getId());
        }

        return ResponseEntity.ok(ApiResponse.ok("My bookings list", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDto>> getBookingById(@PathVariable Long id) {
        BookingDto booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.ok("Booking details", booking));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingDto>> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        String notes = body.get("notes");
        BookingDto updated = bookingService.updateStatus(id, status, notes);
        return ResponseEntity.ok(ApiResponse.ok("Booking status updated to " + status, updated));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingDto>> cancelBooking(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {

        String reason = body != null ? body.get("reason") : "Cancelled by user";
        BookingDto updated = bookingService.cancelBooking(id, userPrincipal.getId(), reason);
        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled successfully", updated));
    }
}
