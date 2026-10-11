package com.buildnear.app.service;

import com.buildnear.app.dto.BookingDto;
import com.buildnear.app.entity.*;
import com.buildnear.app.exception.BadRequestException;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.BookingRepository;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import com.buildnear.app.repository.ReviewRepository;
import com.buildnear.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final ReviewRepository reviewRepository;

    public BookingService(BookingRepository bookingRepository,
                          UserRepository userRepository,
                          ProfessionalProfileRepository professionalProfileRepository,
                          ReviewRepository reviewRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public BookingDto createDirectBooking(Long customerId, Long professionalId, BookingDto dto) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        ProfessionalProfile professional = professionalProfileRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found: " + professionalId));

        String bookingNumber = "BN-" + (System.currentTimeMillis() % 10000000);
        Booking booking = new Booking();
        booking.setBookingNumber(bookingNumber);
        booking.setCustomer(customer);
        booking.setProfessional(professional);
        booking.setServiceTitle(dto.getServiceTitle() != null ? dto.getServiceTitle() : (professional.getCategoryTitle() + " Service Visit"));
        booking.setServiceCategory(professional.getCategory());
        booking.setAgreedPrice(dto.getAgreedPrice() != null ? dto.getAgreedPrice() : professional.getMinimumPrice());
        booking.setPricingUnit(dto.getPricingUnit() != null ? dto.getPricingUnit() : professional.getPricingUnit().name());
        booking.setBookingDate(LocalDate.now());
        booking.setScheduledDate(dto.getScheduledDate() != null ? dto.getScheduledDate() : LocalDate.now().plusDays(1));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCustomerAddress(dto.getCustomerAddress() != null ? dto.getCustomerAddress() : customer.getAddress());
        booking.setCustomerPhone(dto.getCustomerPhone() != null ? dto.getCustomerPhone() : customer.getPhone());
        booking.setCustomerNotes(dto.getCustomerNotes());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);
        return mapToDto(saved);
    }

    public List<BookingDto> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getProfessionalBookings(Long professionalId) {
        return bookingRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
        return mapToDto(booking);
    }

    @Transactional
    public BookingDto updateStatus(Long bookingId, String newStatus, String notes) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        try {
            BookingStatus status = BookingStatus.valueOf(newStatus.toUpperCase());
            booking.setStatus(status);
            if (notes != null && !notes.trim().isEmpty()) {
                booking.setCompletionNotes(notes);
            }
            booking.setUpdatedAt(LocalDateTime.now());
            return mapToDto(bookingRepository.save(booking));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid booking status: " + newStatus);
        }
    }

    @Transactional
    public BookingDto cancelBooking(Long bookingId, Long userId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        if (reason != null) {
            booking.setCompletionNotes("Cancelled: " + reason);
        }
        booking.setUpdatedAt(LocalDateTime.now());
        return mapToDto(bookingRepository.save(booking));
    }

    public BookingDto mapToDto(Booking b) {
        BookingDto dto = new BookingDto();
        dto.setId(b.getId());
        dto.setBookingNumber(b.getBookingNumber());

        if (b.getCustomer() != null) {
            dto.setCustomerId(b.getCustomer().getId());
            dto.setCustomerName(b.getCustomer().getFullName());
            dto.setCustomerEmail(b.getCustomer().getEmail());
            dto.setCustomerPhone(b.getCustomer().getPhone());
            dto.setCustomerAddress(b.getCustomerAddress() != null ? b.getCustomerAddress() : b.getCustomer().getAddress());
        }

        if (b.getProfessional() != null) {
            dto.setProfessionalId(b.getProfessional().getId());
            dto.setProfessionalCategory(b.getProfessional().getCategory());
            dto.setProfessionalCategoryTitle(b.getProfessional().getCategoryTitle());
            dto.setProfessionalAvatarUrl(b.getProfessional().getAvatarUrl());

            if (b.getProfessional().getUser() != null) {
                dto.setProfessionalUserId(b.getProfessional().getUser().getId());
                dto.setProfessionalName(b.getProfessional().getUser().getFullName());
                dto.setProfessionalPhone(b.getProfessional().getUser().getPhone());
            }
        }

        if (b.getServiceRequest() != null) {
            dto.setServiceRequestId(b.getServiceRequest().getId());
        }

        if (b.getQuotation() != null) {
            dto.setQuotationId(b.getQuotation().getId());
        }

        dto.setServiceTitle(b.getServiceTitle());
        dto.setServiceCategory(b.getServiceCategory());
        dto.setAgreedPrice(b.getAgreedPrice());
        dto.setPricingUnit(b.getPricingUnit());
        dto.setBookingDate(b.getBookingDate());
        dto.setScheduledDate(b.getScheduledDate());
        dto.setStatus(b.getStatus() != null ? b.getStatus().name() : "PENDING");
        dto.setCustomerNotes(b.getCustomerNotes());
        dto.setCompletionNotes(b.getCompletionNotes());
        dto.setCreatedAt(b.getCreatedAt());
        dto.setUpdatedAt(b.getUpdatedAt());

        Optional<Review> reviewOpt = reviewRepository.findByBookingId(b.getId());
        if (reviewOpt.isPresent()) {
            dto.setHasReview(true);
            dto.setReviewRating(reviewOpt.get().getRating());
            dto.setReviewComment(reviewOpt.get().getComment());
        } else {
            dto.setHasReview(false);
        }

        return dto;
    }
}
