package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.BookingDto;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.dto.QuotationDto;
import com.buildnear.app.security.UserPrincipal;
import com.buildnear.app.service.ProfessionalService;
import com.buildnear.app.service.QuotationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    private final QuotationService quotationService;
    private final ProfessionalService professionalService;

    public QuotationController(QuotationService quotationService, ProfessionalService professionalService) {
        this.quotationService = quotationService;
        this.professionalService = professionalService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<QuotationDto>> submitQuotation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody QuotationDto dto) {

        ProfessionalProfileDto proProfile = professionalService.getProfileByUserId(userPrincipal.getId());
        QuotationDto submitted = quotationService.submitQuotation(proProfile.getId(), dto);
        return ResponseEntity.ok(ApiResponse.ok("Quotation submitted successfully", submitted));
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<ApiResponse<List<QuotationDto>>> getQuotationsForRequest(@PathVariable Long requestId) {
        List<QuotationDto> quotes = quotationService.getQuotationsForRequest(requestId);
        return ResponseEntity.ok(ApiResponse.ok("Quotations retrieved for comparison", quotes));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<QuotationDto>>> getMyQuotations(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ProfessionalProfileDto proProfile = professionalService.getProfileByUserId(userPrincipal.getId());
        List<QuotationDto> quotes = quotationService.getQuotationsByProfessional(proProfile.getId());
        return ResponseEntity.ok(ApiResponse.ok("My submitted quotations", quotes));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<BookingDto>> acceptQuotation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {

        BookingDto booking = quotationService.acceptQuotation(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Quotation accepted and booking confirmed!", booking));
    }
}
