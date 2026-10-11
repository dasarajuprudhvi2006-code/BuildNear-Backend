package com.buildnear.app.service;

import com.buildnear.app.dto.BookingDto;
import com.buildnear.app.dto.QuotationDto;
import com.buildnear.app.entity.*;
import com.buildnear.app.exception.BadRequestException;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.BookingRepository;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import com.buildnear.app.repository.QuotationRepository;
import com.buildnear.app.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final ServiceRequestRepository requestRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final BookingRepository bookingRepository;

    public QuotationService(QuotationRepository quotationRepository,
                            ServiceRequestRepository requestRepository,
                            ProfessionalProfileRepository professionalProfileRepository,
                            BookingRepository bookingRepository) {
        this.quotationRepository = quotationRepository;
        this.requestRepository = requestRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public QuotationDto submitQuotation(Long professionalId, QuotationDto dto) {
        ServiceRequest req = requestRepository.findById(dto.getServiceRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found: " + dto.getServiceRequestId()));

        ProfessionalProfile prof = professionalProfileRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found: " + professionalId));

        if (req.getStatus() == RequestStatus.CANCELLED || req.getStatus() == RequestStatus.COMPLETED) {
            throw new BadRequestException("Cannot submit quotation for request with status: " + req.getStatus());
        }

        Quotation quotation = new Quotation();
        quotation.setServiceRequest(req);
        quotation.setProfessional(prof);
        
        Double labor = dto.getLaborCost();
        Double material = dto.getMaterialCost();
        Double total = dto.getTotalAmount();

        if (total == null) {
            double l = labor != null ? labor : 0.0;
            double m = material != null ? material : 0.0;
            total = l + m;
        }

        quotation.setTotalAmount(total);
        quotation.setLaborCost(labor != null ? labor : total * 0.4);
        quotation.setMaterialCost(material != null ? material : total * 0.6);
        quotation.setEstimatedDays(dto.getEstimatedDays() != null ? dto.getEstimatedDays() : 7);
        quotation.setBreakdownNotes(dto.getBreakdownNotes());
        quotation.setTermsAndWarranty(dto.getTermsAndWarranty() != null ? dto.getTermsAndWarranty() : "Standard 6-month workmanship warranty included.");
        quotation.setStatus(QuotationStatus.PENDING);
        quotation.setCreatedAt(LocalDateTime.now());

        Quotation saved = quotationRepository.save(quotation);

        // Update request status to QUOTED if still pending
        if (req.getStatus() == RequestStatus.PENDING) {
            req.setStatus(RequestStatus.QUOTED);
            requestRepository.save(req);
        }

        return mapToDto(saved);
    }

    public List<QuotationDto> getQuotationsForRequest(Long requestId) {
        return quotationRepository.findByServiceRequestIdOrderByCreatedAtDesc(requestId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<QuotationDto> getQuotationsByProfessional(Long professionalId) {
        return quotationRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto acceptQuotation(Long quotationId, Long customerId) {
        Quotation winningQuote = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found: " + quotationId));

        ServiceRequest req = winningQuote.getServiceRequest();
        if (!req.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You are not authorized to accept this quotation");
        }

        if (winningQuote.getStatus() == QuotationStatus.ACCEPTED) {
            throw new BadRequestException("Quotation is already accepted");
        }

        // Accept this quotation
        winningQuote.setStatus(QuotationStatus.ACCEPTED);
        quotationRepository.save(winningQuote);

        // Reject other pending quotations for this request
        List<Quotation> otherQuotes = quotationRepository.findByServiceRequestIdOrderByCreatedAtDesc(req.getId());
        for (Quotation q : otherQuotes) {
            if (!q.getId().equals(quotationId) && q.getStatus() == QuotationStatus.PENDING) {
                q.setStatus(QuotationStatus.REJECTED);
                quotationRepository.save(q);
            }
        }

        // Update request status
        req.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(req);

        // Automatically create a Booking
        String bookingNumber = "BN-" + (System.currentTimeMillis() % 10000000);
        Booking booking = new Booking();
        booking.setBookingNumber(bookingNumber);
        booking.setCustomer(req.getCustomer());
        booking.setProfessional(winningQuote.getProfessional());
        booking.setServiceRequest(req);
        booking.setQuotation(winningQuote);
        booking.setServiceTitle(req.getTitle());
        booking.setServiceCategory(req.getCategory());
        booking.setAgreedPrice(winningQuote.getTotalAmount());
        booking.setPricingUnit("PER_PROJECT");
        booking.setBookingDate(LocalDate.now());
        booking.setScheduledDate(req.getPreferredStartDate() != null ? req.getPreferredStartDate() : LocalDate.now().plusDays(2));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCustomerAddress(req.getAddress() != null ? req.getAddress() : req.getCustomer().getAddress());
        booking.setCustomerPhone(req.getCustomer().getPhone());
        booking.setCustomerNotes("Accepted quote #" + quotationId + ": " + winningQuote.getBreakdownNotes());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        BookingDto bDto = new BookingDto();
        bDto.setId(savedBooking.getId());
        bDto.setBookingNumber(savedBooking.getBookingNumber());
        bDto.setServiceTitle(savedBooking.getServiceTitle());
        bDto.setServiceCategory(savedBooking.getServiceCategory());
        bDto.setAgreedPrice(savedBooking.getAgreedPrice());
        bDto.setStatus(savedBooking.getStatus().name());
        bDto.setScheduledDate(savedBooking.getScheduledDate());
        return bDto;
    }

    public QuotationDto mapToDto(Quotation q) {
        QuotationDto dto = new QuotationDto();
        dto.setId(q.getId());

        if (q.getServiceRequest() != null) {
            dto.setServiceRequestId(q.getServiceRequest().getId());
            dto.setServiceRequestTitle(q.getServiceRequest().getTitle());
        }

        if (q.getProfessional() != null) {
            dto.setProfessionalId(q.getProfessional().getId());
            dto.setProfessionalCategory(q.getProfessional().getCategory());
            dto.setProfessionalCategoryTitle(q.getProfessional().getCategoryTitle());
            dto.setProfessionalRating(q.getProfessional().getAverageRating());
            dto.setProfessionalReviewsCount(q.getProfessional().getReviewCount());
            dto.setProfessionalAvatarUrl(q.getProfessional().getAvatarUrl());

            if (q.getProfessional().getUser() != null) {
                dto.setProfessionalUserId(q.getProfessional().getUser().getId());
                dto.setProfessionalName(q.getProfessional().getUser().getFullName());
                dto.setProfessionalPhone(q.getProfessional().getUser().getPhone());
                dto.setProfessionalCity(q.getProfessional().getUser().getCity());
            }
        }

        dto.setTotalAmount(q.getTotalAmount());
        dto.setLaborCost(q.getLaborCost());
        dto.setMaterialCost(q.getMaterialCost());
        dto.setEstimatedDays(q.getEstimatedDays());
        dto.setBreakdownNotes(q.getBreakdownNotes());
        dto.setTermsAndWarranty(q.getTermsAndWarranty());
        dto.setStatus(q.getStatus() != null ? q.getStatus().name() : "PENDING");
        dto.setCreatedAt(q.getCreatedAt());

        return dto;
    }
}
