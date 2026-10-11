package com.buildnear.app.repository;

import com.buildnear.app.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    List<ServicePackage> findByCategoryIgnoreCase(String category);
    List<ServicePackage> findByPopularTrue();
    Optional<ServicePackage> findByPackageCode(String packageCode);
}
