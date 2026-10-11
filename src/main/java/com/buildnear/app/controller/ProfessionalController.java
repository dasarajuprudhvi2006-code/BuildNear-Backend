package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.entity.PortfolioItem;
import com.buildnear.app.security.UserPrincipal;
import com.buildnear.app.service.ProfessionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfessionalProfileDto>>> searchProfessionals(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy) {

        List<ProfessionalProfileDto> results = professionalService.searchProfessionals(
                lat, lng, radius, category, city, minPrice, maxPrice, minRating, minExperience, search, sortBy
        );

        return ResponseEntity.ok(ApiResponse.ok("Professionals retrieved", results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfessionalProfileDto>> getProfessionalById(
            @PathVariable Long id,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng) {

        ProfessionalProfileDto dto = professionalService.getProfileById(id, lat, lng);
        return ResponseEntity.ok(ApiResponse.ok("Professional details", dto));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfessionalProfileDto>> getMyProfessionalProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ProfessionalProfileDto dto = professionalService.getProfileByUserId(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Professional profile details", dto));
    }

    @PutMapping("/availability")
    public ResponseEntity<ApiResponse<ProfessionalProfileDto>> toggleAvailability(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        ProfessionalProfileDto myProfile = professionalService.getProfileByUserId(userPrincipal.getId());
        ProfessionalProfileDto updated = professionalService.toggleAvailability(myProfile.getId());
        return ResponseEntity.ok(ApiResponse.ok("Availability updated", updated));
    }

    @PostMapping("/portfolio")
    public ResponseEntity<ApiResponse<PortfolioItem>> addPortfolioItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody PortfolioItem item) {

        ProfessionalProfileDto myProfile = professionalService.getProfileByUserId(userPrincipal.getId());
        PortfolioItem saved = professionalService.addPortfolioItem(myProfile.getId(), item);
        return ResponseEntity.ok(ApiResponse.ok("Portfolio item added", saved));
    }

    @DeleteMapping("/portfolio/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deletePortfolioItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long itemId) {

        professionalService.deletePortfolioItem(itemId);
        return ResponseEntity.ok(ApiResponse.ok("Portfolio item deleted", null));
    }
}
