package com.buildnear.app.repository;

import com.buildnear.app.entity.ProfessionalProfile;
import com.buildnear.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalProfileRepository extends JpaRepository<ProfessionalProfile, Long> {
    Optional<ProfessionalProfile> findByUser(User user);
    Optional<ProfessionalProfile> findByUserId(Long userId);
    List<ProfessionalProfile> findByCategoryIgnoreCase(String category);
    List<ProfessionalProfile> findByVerifiedByAdmin(boolean verifiedByAdmin);
}
