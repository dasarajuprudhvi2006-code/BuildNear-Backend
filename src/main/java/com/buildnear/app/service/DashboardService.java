package com.buildnear.app.service;

import com.buildnear.app.dto.DashboardStatsDto;
import com.buildnear.app.entity.Booking;
import com.buildnear.app.entity.BookingStatus;
import com.buildnear.app.entity.ProfessionalProfile;
import com.buildnear.app.entity.Role;
import com.buildnear.app.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRequestRepository requestRepository;
    private final QuotationRepository quotationRepository;
    private final ReviewRepository reviewRepository;

    public DashboardService(UserRepository userRepository,
                            ProfessionalProfileRepository professionalProfileRepository,
                            BookingRepository bookingRepository,
                            ServiceRequestRepository requestRepository,
                            QuotationRepository quotationRepository,
                            ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.bookingRepository = bookingRepository;
        this.requestRepository = requestRepository;
        this.quotationRepository = quotationRepository;
        this.reviewRepository = reviewRepository;
    }

    public DashboardStatsDto getAdminStats() {
        DashboardStatsDto dto = new DashboardStatsDto();

        dto.setTotalUsers(userRepository.count());
        dto.setTotalCustomers(userRepository.findByRole(Role.ROLE_CUSTOMER).size());
        List<ProfessionalProfile> allPros = professionalProfileRepository.findAll();
        dto.setTotalProfessionals(allPros.size());

        long verified = allPros.stream().filter(ProfessionalProfile::isVerifiedByAdmin).count();
        dto.setVerifiedProfessionals(verified);
        dto.setPendingVerifications(allPros.size() - verified);

        List<Booking> allBookings = bookingRepository.findAll();
        dto.setTotalBookings(allBookings.size());
        dto.setActiveBookings(allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.IN_PROGRESS || b.getStatus() == BookingStatus.PENDING)
                .count());
        dto.setCompletedBookings(allBookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count());
        dto.setCancelledBookings(allBookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count());

        dto.setTotalServiceRequests(requestRepository.count());
        dto.setTotalQuotations(quotationRepository.count());
        dto.setTotalReviews(reviewRepository.count());

        double avgRating = allPros.stream().filter(p -> p.getAverageRating() > 0)
                .mapToDouble(ProfessionalProfile::getAverageRating)
                .average()
                .orElse(4.8);
        dto.setAveragePlatformRating(Math.round(avgRating * 10.0) / 10.0);

        double totalVol = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.IN_PROGRESS)
                .mapToDouble(Booking::getAgreedPrice)
                .sum();
        dto.setTotalTransactionVolume(totalVol);

        // Pros by category
        Map<String, Long> byCat = allPros.stream()
                .collect(Collectors.groupingBy(ProfessionalProfile::getCategory, Collectors.counting()));
        dto.setProfessionalsByCategory(byCat);

        // Bookings by status
        Map<String, Long> byStatus = allBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getStatus().name(), Collectors.counting()));
        dto.setBookingsByStatus(byStatus);

        return dto;
    }

    public Map<String, Object> getCustomerStats(Long customerId) {
        Map<String, Object> map = new HashMap<>();
        var requests = requestRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        var bookings = bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        var reviews = reviewRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        map.put("totalRequests", requests.size());
        map.put("totalBookings", bookings.size());
        map.put("activeBookings", bookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.IN_PROGRESS).count());
        map.put("completedBookings", bookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count());
        map.put("reviewsGiven", reviews.size());
        return map;
    }

    public Map<String, Object> getProfessionalStats(Long professionalId) {
        Map<String, Object> map = new HashMap<>();
        var profile = professionalProfileRepository.findById(professionalId).orElse(null);
        var quotes = quotationRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId);
        var bookings = bookingRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId);
        var reviews = reviewRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId);

        long activeJobs = bookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.IN_PROGRESS).count();
        long completedJobs = bookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count();
        double totalEarnings = bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .mapToDouble(Booking::getAgreedPrice)
                .sum();

        map.put("quotesSubmitted", quotes.size());
        map.put("activeJobs", activeJobs);
        map.put("completedJobs", completedJobs);
        map.put("totalEarnings", totalEarnings);
        map.put("totalReviews", reviews.size());
        map.put("rating", profile != null ? profile.getAverageRating() : 0.0);
        map.put("availability", profile != null ? profile.getAvailability().name() : "AVAILABLE");
        map.put("verified", profile != null && profile.isVerifiedByAdmin());
        return map;
    }
}
