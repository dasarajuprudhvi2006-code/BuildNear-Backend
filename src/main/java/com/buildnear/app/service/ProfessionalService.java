package com.buildnear.app.service;

import com.buildnear.app.dto.ProfessionalProfileDto;
import com.buildnear.app.entity.AvailabilityStatus;
import com.buildnear.app.entity.PortfolioItem;
import com.buildnear.app.entity.ProfessionalProfile;
import com.buildnear.app.entity.User;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.PortfolioItemRepository;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionalService {

    private final ProfessionalProfileRepository profileRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public ProfessionalService(ProfessionalProfileRepository profileRepository,
                               PortfolioItemRepository portfolioItemRepository) {
        this.profileRepository = profileRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((R * c) * 10.0) / 10.0;
    }

    public List<ProfessionalProfileDto> searchProfessionals(
            Double userLat,
            Double userLng,
            Double radiusKm,
            String category,
            String city,
            Double minPrice,
            Double maxPrice,
            Double minRating,
            Integer minExperience,
            String searchKeyword,
            String sortBy) {

        List<ProfessionalProfile> allProfiles = profileRepository.findAll();

        return allProfiles.stream()
                .filter(p -> p.getUser() != null && p.getUser().isActive())
                // Category filter
                .filter(p -> category == null || category.trim().isEmpty() || "ALL".equalsIgnoreCase(category)
                        || p.getCategory().equalsIgnoreCase(category))
                // City filter
                .filter(p -> city == null || city.trim().isEmpty()
                        || (p.getUser().getCity() != null && p.getUser().getCity().toLowerCase().contains(city.toLowerCase().trim())))
                // Min price
                .filter(p -> minPrice == null || p.getMinimumPrice() >= minPrice)
                // Max price
                .filter(p -> maxPrice == null || p.getMinimumPrice() <= maxPrice)
                // Min rating
                .filter(p -> minRating == null || p.getAverageRating() >= minRating)
                // Min experience
                .filter(p -> minExperience == null || p.getExperienceYears() >= minExperience)
                // Keyword search in name, skills, bio, qualifications, title, city, category
                .filter(p -> {
                    if (searchKeyword == null || searchKeyword.trim().isEmpty()) return true;
                    String q = searchKeyword.toLowerCase().trim();
                    boolean inName = p.getUser().getFullName() != null && p.getUser().getFullName().toLowerCase().contains(q);
                    boolean inSkills = p.getSkills() != null && p.getSkills().toLowerCase().contains(q);
                    boolean inBio = p.getBio() != null && p.getBio().toLowerCase().contains(q);
                    boolean inQual = p.getQualifications() != null && p.getQualifications().toLowerCase().contains(q);
                    boolean inTitle = p.getCategoryTitle() != null && p.getCategoryTitle().toLowerCase().contains(q);
                    boolean inCategory = p.getCategory() != null && p.getCategory().toLowerCase().contains(q);
                    boolean inCity = p.getUser().getCity() != null && p.getUser().getCity().toLowerCase().contains(q);
                    return inName || inSkills || inBio || inQual || inTitle || inCategory || inCity;
                })
                .map(p -> {
                    ProfessionalProfileDto dto = mapToDto(p);
                    if (userLat != null && userLng != null && p.getUser().getLatitude() != null && p.getUser().getLongitude() != null) {
                        double distance = calculateDistanceKm(userLat, userLng, p.getUser().getLatitude(), p.getUser().getLongitude());
                        dto.setDistanceKm(distance);
                    }
                    return dto;
                })
                // Radius filter — ONLY applied when no keyword search is active
                .filter(dto -> {
                    // If user typed a keyword, show results across all cities (no radius restriction)
                    if (searchKeyword != null && !searchKeyword.trim().isEmpty()) return true;
                    if (radiusKm == null || radiusKm <= 0 || userLat == null || userLng == null) return true;
                    return dto.getDistanceKm() != null && dto.getDistanceKm() <= radiusKm;
                })
                // Sorting
                .sorted((a, b) -> {
                    String sort = sortBy != null ? sortBy.toLowerCase() : "rating_desc";
                    switch (sort) {
                        case "distance_asc":
                            if (a.getDistanceKm() == null) return 1;
                            if (b.getDistanceKm() == null) return -1;
                            return Double.compare(a.getDistanceKm(), b.getDistanceKm());
                        case "price_asc":
                            return Double.compare(a.getMinimumPrice(), b.getMinimumPrice());
                        case "price_desc":
                            return Double.compare(b.getMinimumPrice(), a.getMinimumPrice());
                        case "experience_desc":
                            return Integer.compare(b.getExperienceYears(), a.getExperienceYears());
                        case "rating_desc":
                        default:
                            return Double.compare(b.getAverageRating(), a.getAverageRating());
                    }
                })
                .collect(Collectors.toList());
    }

    public ProfessionalProfileDto getProfileById(Long id, Double userLat, Double userLng) {
        ProfessionalProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + id));

        ProfessionalProfileDto dto = mapToDto(profile);
        if (userLat != null && userLng != null && profile.getUser().getLatitude() != null && profile.getUser().getLongitude() != null) {
            dto.setDistanceKm(calculateDistanceKm(userLat, userLng, profile.getUser().getLatitude(), profile.getUser().getLongitude()));
        }
        return dto;
    }

    public ProfessionalProfile getEntityById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + id));
    }

    public ProfessionalProfileDto getProfileByUserId(Long userId) {
        ProfessionalProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional profile not found for user id: " + userId));
        return mapToDto(profile);
    }

    @Transactional
    public ProfessionalProfileDto toggleAvailability(Long professionalId) {
        ProfessionalProfile profile = getEntityById(professionalId);
        if (profile.getAvailability() == AvailabilityStatus.AVAILABLE) {
            profile.setAvailability(AvailabilityStatus.BUSY);
        } else {
            profile.setAvailability(AvailabilityStatus.AVAILABLE);
        }
        return mapToDto(profileRepository.save(profile));
    }

    @Transactional
    public ProfessionalProfileDto setVerificationStatus(Long professionalId, boolean verified) {
        ProfessionalProfile profile = getEntityById(professionalId);
        profile.setVerifiedByAdmin(verified);
        return mapToDto(profileRepository.save(profile));
    }

    @Transactional
    public PortfolioItem addPortfolioItem(Long professionalId, PortfolioItem item) {
        ProfessionalProfile profile = getEntityById(professionalId);
        item.setProfessional(profile);
        return portfolioItemRepository.save(item);
    }

    @Transactional
    public void deletePortfolioItem(Long itemId) {
        if (!portfolioItemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException("Portfolio item not found with id: " + itemId);
        }
        portfolioItemRepository.deleteById(itemId);
    }

    public ProfessionalProfileDto mapToDto(ProfessionalProfile profile) {
        ProfessionalProfileDto dto = new ProfessionalProfileDto();
        dto.setId(profile.getId());

        User user = profile.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setCity(user.getCity());
            dto.setAddress(user.getAddress());
            dto.setLatitude(user.getLatitude());
            dto.setLongitude(user.getLongitude());
        }

        dto.setCategory(profile.getCategory());
        dto.setCategoryTitle(profile.getCategoryTitle());
        dto.setBio(profile.getBio());
        dto.setExperienceYears(profile.getExperienceYears());
        dto.setQualifications(profile.getQualifications());
        dto.setSkills(profile.getSkills());
        dto.setMinimumPrice(profile.getMinimumPrice());
        dto.setPricingUnit(profile.getPricingUnit() != null ? profile.getPricingUnit().name() : "PER_DAY");
        dto.setServiceRadiusKm(profile.getServiceRadiusKm());
        dto.setAvailability(profile.getAvailability() != null ? profile.getAvailability().name() : "AVAILABLE");
        dto.setVerifiedByAdmin(profile.isVerifiedByAdmin());
        dto.setAverageRating(profile.getAverageRating());
        dto.setReviewCount(profile.getReviewCount());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setYearsInArea(profile.getYearsInArea());

        List<PortfolioItem> items = portfolioItemRepository.findByProfessionalId(profile.getId());
        dto.setPortfolioItems(items);

        return dto;
    }
}
