package com.buildnear.app.service;

import com.buildnear.app.dto.ReviewDto;
import com.buildnear.app.entity.Booking;
import com.buildnear.app.entity.BookingStatus;
import com.buildnear.app.entity.ProfessionalProfile;
import com.buildnear.app.entity.Review;
import com.buildnear.app.exception.BadRequestException;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.BookingRepository;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import com.buildnear.app.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         BookingRepository bookingRepository,
                         ProfessionalProfileRepository professionalProfileRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.professionalProfileRepository = professionalProfileRepository;
    }

    @Transactional
    public ReviewDto createReview(Long customerId, ReviewDto dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + dto.getBookingId()));

        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You are not authorized to review this booking");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BadRequestException("You can only review a booking that is marked COMPLETED");
        }

        if (reviewRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new BadRequestException("A review has already been submitted for this booking");
        }

        ProfessionalProfile prof = booking.getProfessional();

        Review review = new Review();
        review.setBooking(booking);
        review.setCustomer(booking.getCustomer());
        review.setProfessional(prof);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // Recalculate professional rating
        List<Review> allReviews = reviewRepository.findByProfessionalIdOrderByCreatedAtDesc(prof.getId());
        double avg = allReviews.stream().mapToInt(Review::getRating).average().orElse(dto.getRating());
        avg = Math.round(avg * 10.0) / 10.0;

        prof.setAverageRating(avg);
        prof.setReviewCount(allReviews.size());
        professionalProfileRepository.save(prof);

        return mapToDto(saved);
    }

    public List<ReviewDto> getReviewsForProfessional(Long professionalId) {
        return reviewRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto respondToReview(Long reviewId, Long professionalId, String response) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));

        if (!review.getProfessional().getId().equals(professionalId)) {
            throw new BadRequestException("You are not authorized to respond to this review");
        }

        review.setProfessionalResponse(response);
        return mapToDto(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));

        ProfessionalProfile prof = review.getProfessional();
        reviewRepository.delete(review);

        // Recalculate
        List<Review> remaining = reviewRepository.findByProfessionalIdOrderByCreatedAtDesc(prof.getId());
        if (remaining.isEmpty()) {
            prof.setAverageRating(0.0);
            prof.setReviewCount(0);
        } else {
            double avg = remaining.stream().mapToInt(Review::getRating).average().orElse(0.0);
            prof.setAverageRating(Math.round(avg * 10.0) / 10.0);
            prof.setReviewCount(remaining.size());
        }
        professionalProfileRepository.save(prof);
    }

    public ReviewDto mapToDto(Review r) {
        ReviewDto dto = new ReviewDto();
        dto.setId(r.getId());
        if (r.getBooking() != null) {
            dto.setBookingId(r.getBooking().getId());
        }
        if (r.getCustomer() != null) {
            dto.setCustomerId(r.getCustomer().getId());
            dto.setCustomerName(r.getCustomer().getFullName());
        }
        if (r.getProfessional() != null) {
            dto.setProfessionalId(r.getProfessional().getId());
            if (r.getProfessional().getUser() != null) {
                dto.setProfessionalName(r.getProfessional().getUser().getFullName());
            }
        }
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setProfessionalResponse(r.getProfessionalResponse());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
