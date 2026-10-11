package com.buildnear.app.controller;

import com.buildnear.app.dto.ApiResponse;
import com.buildnear.app.entity.ServicePackage;
import com.buildnear.app.service.ServicePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class ServicePackageController {

    private final ServicePackageService packageService;

    public ServicePackageController(ServicePackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServicePackage>>> getPackages(
            @RequestParam(required = false) String category) {
        List<ServicePackage> packages = packageService.getPackagesByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok("Service packages retrieved", packages));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<ServicePackage>>> getPopularPackages() {
        List<ServicePackage> packages = packageService.getPopularPackages();
        return ResponseEntity.ok(ApiResponse.ok("Popular service packages retrieved", packages));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServicePackage>> getPackageById(@PathVariable Long id) {
        ServicePackage sp = packageService.getPackageById(id);
        return ResponseEntity.ok(ApiResponse.ok("Service package details", sp));
    }
}
