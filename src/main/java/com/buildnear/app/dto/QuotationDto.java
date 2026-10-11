package com.buildnear.app.dto;

import java.time.LocalDateTime;

public class QuotationDto {

    private Long id;
    private Long serviceRequestId;
    private String serviceRequestTitle;
    private Long professionalId;
    private Long professionalUserId;
    private String professionalName;
    private String professionalPhone;
    private String professionalCategory;
    private String professionalCategoryTitle;
    private Double professionalRating;
    private Integer professionalReviewsCount;
    private String professionalAvatarUrl;
    private String professionalCity;
    private Double totalAmount;
    private Double laborCost;
    private Double materialCost;
    private Integer estimatedDays;
    private String breakdownNotes;
    private String termsAndWarranty;
    private String status;
    private LocalDateTime createdAt;

    public QuotationDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getServiceRequestTitle() {
        return serviceRequestTitle;
    }

    public void setServiceRequestTitle(String serviceRequestTitle) {
        this.serviceRequestTitle = serviceRequestTitle;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public Long getProfessionalUserId() {
        return professionalUserId;
    }

    public void setProfessionalUserId(Long professionalUserId) {
        this.professionalUserId = professionalUserId;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public String getProfessionalPhone() {
        return professionalPhone;
    }

    public void setProfessionalPhone(String professionalPhone) {
        this.professionalPhone = professionalPhone;
    }

    public String getProfessionalCategory() {
        return professionalCategory;
    }

    public void setProfessionalCategory(String professionalCategory) {
        this.professionalCategory = professionalCategory;
    }

    public String getProfessionalCategoryTitle() {
        return professionalCategoryTitle;
    }

    public void setProfessionalCategoryTitle(String professionalCategoryTitle) {
        this.professionalCategoryTitle = professionalCategoryTitle;
    }

    public Double getProfessionalRating() {
        return professionalRating;
    }

    public void setProfessionalRating(Double professionalRating) {
        this.professionalRating = professionalRating;
    }

    public Integer getProfessionalReviewsCount() {
        return professionalReviewsCount;
    }

    public void setProfessionalReviewsCount(Integer professionalReviewsCount) {
        this.professionalReviewsCount = professionalReviewsCount;
    }

    public String getProfessionalAvatarUrl() {
        return professionalAvatarUrl;
    }

    public void setProfessionalAvatarUrl(String professionalAvatarUrl) {
        this.professionalAvatarUrl = professionalAvatarUrl;
    }

    public String getProfessionalCity() {
        return professionalCity;
    }

    public void setProfessionalCity(String professionalCity) {
        this.professionalCity = professionalCity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
