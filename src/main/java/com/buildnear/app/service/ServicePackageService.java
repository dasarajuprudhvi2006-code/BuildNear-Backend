package com.buildnear.app.service;

import com.buildnear.app.entity.ServicePackage;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.ServicePackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePackageService {

    private final ServicePackageRepository packageRepository;

    public ServicePackageService(ServicePackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public List<ServicePackage> getAllPackages() {
        return packageRepository.findAll();
    }

    public List<ServicePackage> getPackagesByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("ALL")) {
            return packageRepository.findAll();
        }
        return packageRepository.findByCategoryIgnoreCase(category);
    }

    public List<ServicePackage> getPopularPackages() {
        return packageRepository.findByPopularTrue();
    }

    public ServicePackage getPackageById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service package not found with id: " + id));
    }
}
