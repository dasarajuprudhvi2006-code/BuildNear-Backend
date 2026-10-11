package com.buildnear.app.service;

import com.buildnear.app.dto.UserProfileUpdateDto;
import com.buildnear.app.entity.AvailabilityStatus;
import com.buildnear.app.entity.PricingUnit;
import com.buildnear.app.entity.ProfessionalProfile;
import com.buildnear.app.entity.User;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import com.buildnear.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;

    public UserService(UserRepository userRepository, ProfessionalProfileRepository professionalProfileRepository) {
        this.userRepository = userRepository;
        this.professionalProfileRepository = professionalProfileRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public User updateProfile(Long userId, UserProfileUpdateDto dto) {
        User user = getUserById(userId);

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getCity() != null) user.setCity(dto.getCity());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getLatitude() != null) user.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) user.setLongitude(dto.getLongitude());

        userRepository.save(user);

        // If user has a professional profile, update that as well
        professionalProfileRepository.findByUser(user).ifPresent(profile -> {
            if (dto.getBio() != null) profile.setBio(dto.getBio());
            if (dto.getCategoryTitle() != null) profile.setCategoryTitle(dto.getCategoryTitle());
            if (dto.getSkills() != null) profile.setSkills(dto.getSkills());
            if (dto.getQualifications() != null) profile.setQualifications(dto.getQualifications());
            if (dto.getExperienceYears() != null) profile.setExperienceYears(dto.getExperienceYears());
            if (dto.getMinimumPrice() != null) profile.setMinimumPrice(dto.getMinimumPrice());
            if (dto.getServiceRadiusKm() != null) profile.setServiceRadiusKm(dto.getServiceRadiusKm());
            if (dto.getAvatarUrl() != null) profile.setAvatarUrl(dto.getAvatarUrl());

            if (dto.getPricingUnit() != null) {
                try {
                    profile.setPricingUnit(PricingUnit.valueOf(dto.getPricingUnit()));
                } catch (IllegalArgumentException ignored) {}
            }
            if (dto.getAvailability() != null) {
                try {
                    profile.setAvailability(AvailabilityStatus.valueOf(dto.getAvailability()));
                } catch (IllegalArgumentException ignored) {}
            }
            professionalProfileRepository.save(profile);
        });

        return user;
    }

    @Transactional
    public User toggleUserStatus(Long userId) {
        User user = getUserById(userId);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }
}
