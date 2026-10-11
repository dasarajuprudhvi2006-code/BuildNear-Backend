package com.buildnear.app.service;

import com.buildnear.app.dto.QuotationDto;
import com.buildnear.app.dto.ServiceRequestDto;
import com.buildnear.app.entity.ProfessionalProfile;
import com.buildnear.app.entity.RequestStatus;
import com.buildnear.app.entity.ServiceRequest;
import com.buildnear.app.entity.User;
import com.buildnear.app.exception.BadRequestException;
import com.buildnear.app.exception.ResourceNotFoundException;
import com.buildnear.app.repository.ProfessionalProfileRepository;
import com.buildnear.app.repository.QuotationRepository;
import com.buildnear.app.repository.ServiceRequestRepository;
import com.buildnear.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final QuotationRepository quotationRepository;

    public ServiceRequestService(ServiceRequestRepository requestRepository,
                                 UserRepository userRepository,
                                 ProfessionalProfileRepository professionalProfileRepository,
                                 QuotationRepository quotationRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.quotationRepository = quotationRepository;
    }

    @Transactional
    public ServiceRequestDto createRequest(Long customerId, ServiceRequestDto dto) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        ServiceRequest request = new ServiceRequest();
        request.setCustomer(customer);
        request.setCategory(dto.getCategory() != null ? dto.getCategory() : "GENERAL_CONSTRUCTION");
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setPropertyType(dto.getPropertyType() != null ? dto.getPropertyType() : "Residential");
        request.setAreaSqFt(dto.getAreaSqFt());
        request.setEstimatedBudget(dto.getEstimatedBudget());
        request.setAddress(dto.getAddress() != null ? dto.getAddress() : customer.getAddress());
        request.setCity(dto.getCity() != null ? dto.getCity() : customer.getCity());
        request.setLatitude(dto.getLatitude() != null ? dto.getLatitude() : customer.getLatitude());
        request.setLongitude(dto.getLongitude() != null ? dto.getLongitude() : customer.getLongitude());
        request.setPreferredStartDate(dto.getPreferredStartDate());
        request.setUrgency(dto.getUrgency() != null ? dto.getUrgency() : "FLEXIBLE");
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        if (dto.getTargetProfessionalId() != null) {
            ProfessionalProfile target = professionalProfileRepository.findById(dto.getTargetProfessionalId())
                    .orElse(null);
            request.setTargetProfessional(target);
        }

        ServiceRequest saved = requestRepository.save(request);
        return mapToDto(saved);
    }

    public List<ServiceRequestDto> getCustomerRequests(Long customerId) {
        return requestRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ServiceRequestDto> getRequestsAvailableForProfessional(Long professionalId) {
        ProfessionalProfile professional = professionalProfileRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found: " + professionalId));

        // Get requests either targeted directly to this professional OR open requests in their category
        return requestRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(req -> req.getStatus() != RequestStatus.CANCELLED && req.getStatus() != RequestStatus.COMPLETED)
                .filter(req -> {
                    boolean isTargeted = req.getTargetProfessional() != null && req.getTargetProfessional().getId().equals(professionalId);
                    boolean isOpenInCategory = req.getTargetProfessional() == null &&
                            (req.getCategory().equalsIgnoreCase(professional.getCategory()) || "ALL".equalsIgnoreCase(req.getCategory()));
                    return isTargeted || isOpenInCategory;
                })
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ServiceRequestDto getRequestById(Long id) {
        ServiceRequest req = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found: " + id));
        return mapToDto(req);
    }

    @Transactional
    public ServiceRequestDto cancelRequest(Long requestId, Long customerId) {
        ServiceRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Service request not found: " + requestId));

        if (!req.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You are not authorized to cancel this request");
        }

        req.setStatus(RequestStatus.CANCELLED);
        return mapToDto(requestRepository.save(req));
    }

    public List<ServiceRequestDto> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ServiceRequestDto mapToDto(ServiceRequest req) {
        ServiceRequestDto dto = new ServiceRequestDto();
        dto.setId(req.getId());

        if (req.getCustomer() != null) {
            dto.setCustomerId(req.getCustomer().getId());
            dto.setCustomerName(req.getCustomer().getFullName());
            dto.setCustomerEmail(req.getCustomer().getEmail());
            dto.setCustomerPhone(req.getCustomer().getPhone());
            dto.setCustomerCity(req.getCustomer().getCity());
        }

        if (req.getTargetProfessional() != null) {
            dto.setTargetProfessionalId(req.getTargetProfessional().getId());
            if (req.getTargetProfessional().getUser() != null) {
                dto.setTargetProfessionalName(req.getTargetProfessional().getUser().getFullName());
            }
        }

        dto.setCategory(req.getCategory());
        dto.setTitle(req.getTitle());
        dto.setDescription(req.getDescription());
        dto.setPropertyType(req.getPropertyType());
        dto.setAreaSqFt(req.getAreaSqFt());
        dto.setEstimatedBudget(req.getEstimatedBudget());
        dto.setAddress(req.getAddress());
        dto.setCity(req.getCity());
        dto.setLatitude(req.getLatitude());
        dto.setLongitude(req.getLongitude());
        dto.setPreferredStartDate(req.getPreferredStartDate());
        dto.setUrgency(req.getUrgency());
        dto.setStatus(req.getStatus() != null ? req.getStatus().name() : "PENDING");
        dto.setCreatedAt(req.getCreatedAt());

        var quotes = quotationRepository.findByServiceRequestIdOrderByCreatedAtDesc(req.getId());
        dto.setQuotationsCount(quotes.size());

        List<QuotationDto> quotationDtos = quotes.stream().map(q -> {
            QuotationDto qDto = new QuotationDto();
            qDto.setId(q.getId());
            qDto.setServiceRequestId(req.getId());
            qDto.setServiceRequestTitle(req.getTitle());
            qDto.setTotalAmount(q.getTotalAmount());
            qDto.setLaborCost(q.getLaborCost());
            qDto.setMaterialCost(q.getMaterialCost());
            qDto.setEstimatedDays(q.getEstimatedDays());
            qDto.setBreakdownNotes(q.getBreakdownNotes());
            qDto.setTermsAndWarranty(q.getTermsAndWarranty());
            qDto.setStatus(q.getStatus() != null ? q.getStatus().name() : "PENDING");
            qDto.setCreatedAt(q.getCreatedAt());

            if (q.getProfessional() != null) {
                qDto.setProfessionalId(q.getProfessional().getId());
                qDto.setProfessionalCategory(q.getProfessional().getCategory());
                qDto.setProfessionalCategoryTitle(q.getProfessional().getCategoryTitle());
                qDto.setProfessionalRating(q.getProfessional().getAverageRating());
                qDto.setProfessionalReviewsCount(q.getProfessional().getReviewCount());
                qDto.setProfessionalAvatarUrl(q.getProfessional().getAvatarUrl());

                if (q.getProfessional().getUser() != null) {
                    qDto.setProfessionalUserId(q.getProfessional().getUser().getId());
                    qDto.setProfessionalName(q.getProfessional().getUser().getFullName());
                    qDto.setProfessionalPhone(q.getProfessional().getUser().getPhone());
                    qDto.setProfessionalCity(q.getProfessional().getUser().getCity());
                }
            }
            return qDto;
        }).collect(Collectors.toList());

        dto.setQuotations(quotationDtos);
        return dto;
    }
}
