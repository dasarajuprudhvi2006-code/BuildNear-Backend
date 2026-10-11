package com.buildnear.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotations")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_request_id", nullable = false)
    @JsonIgnoreProperties("quotations")
    private ServiceRequest serviceRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professional_id", nullable = false)
    @JsonIgnoreProperties({"portfolioItems"})
    private ProfessionalProfile professional;

    @Column(nullable = false)
    private Double totalAmount;

    private Double laborCost;

    private Double materialCost;

    private Integer estimatedDays;

    @Column(columnDefinition = "TEXT")
    private String breakdownNotes;

    @Column(columnDefinition = "TEXT")
    private String termsAndWarranty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuotationStatus status = QuotationStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Quotation() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public ProfessionalProfile getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalProfile professional) {
        this.professional = professional;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(Double laborCost) {
        this.laborCost = laborCost;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getBreakdownNotes() {
        return breakdownNotes;
    }

    public void setBreakdownNotes(String breakdownNotes) {
        this.breakdownNotes = breakdownNotes;
    }

    public String getTermsAndWarranty() {
        return termsAndWarranty;
    }

    public void setTermsAndWarranty(String termsAndWarranty) {
        this.termsAndWarranty = termsAndWarranty;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
