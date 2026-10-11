package com.buildnear.app.service;

import com.buildnear.app.dto.AuthRequest;
import com.buildnear.app.dto.AuthResponse;
import com.buildnear.app.dto.RegisterRequest;
import com.buildnear.app.entity.*;
import com.buildnear.app.exception.BadRequestException;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import com.buildnear.app.repository.UserRepository;
import com.buildnear.app.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       ProfessionalProfileRepository professionalProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        Long proProfileId = null;
        if (user.getRole() == Role.ROLE_PROFESSIONAL) {
            proProfileId = professionalProfileRepository.findByUser(user)
                    .map(ProfessionalProfile::getId)
                    .orElse(null);
        }

        return new AuthResponse(
                jwt,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.getCity(),
                proProfileId
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }

        Role role = Role.ROLE_CUSTOMER;
        if (request.getRole() != null && (request.getRole().equalsIgnoreCase("PROFESSIONAL") || request.getRole().equalsIgnoreCase("ROLE_PROFESSIONAL"))) {
            role = Role.ROLE_PROFESSIONAL;
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName(),
                request.getPhone(),
                role,
                request.getCity(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude()
        );

        User savedUser = userRepository.save(user);
        Long proProfileId = null;

        if (role == Role.ROLE_PROFESSIONAL) {
            ProfessionalProfile profile = new ProfessionalProfile();
            profile.setUser(savedUser);
            profile.setCategory(request.getCategory() != null ? request.getCategory() : "BUILDER_CONTRACTOR");
            profile.setCategoryTitle(request.getCategoryTitle() != null ? request.getCategoryTitle() : "General Contractor");
            profile.setBio(request.getBio() != null ? request.getBio() : "Experienced professional dedicated to quality craftsmanship.");
            profile.setExperienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 2);
            profile.setQualifications(request.getQualifications() != null ? request.getQualifications() : "Certified Tradesman");
            profile.setSkills(request.getSkills() != null ? request.getSkills() : "Construction, Maintenance, Repair");
            profile.setMinimumPrice(request.getMinimumPrice() != null ? request.getMinimumPrice() : 500.0);

            if (request.getPricingUnit() != null) {
                try {
                    profile.setPricingUnit(PricingUnit.valueOf(request.getPricingUnit()));
                } catch (IllegalArgumentException e) {
                    profile.setPricingUnit(PricingUnit.PER_DAY);
                }
            } else {
                profile.setPricingUnit(PricingUnit.PER_DAY);
            }

            profile.setServiceRadiusKm(request.getServiceRadiusKm() != null ? request.getServiceRadiusKm() : 25.0);
            profile.setAvailability(AvailabilityStatus.AVAILABLE);
            profile.setVerifiedByAdmin(false);
            profile.setAverageRating(0.0);
            profile.setReviewCount(0);
            profile.setYearsInArea(2);

            ProfessionalProfile savedProfile = professionalProfileRepository.save(profile);
            proProfileId = savedProfile.getId();
        }

        // Authenticate new user and generate token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return new AuthResponse(
                jwt,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().name(),
                savedUser.getCity(),
                proProfileId
        );
    }
}
