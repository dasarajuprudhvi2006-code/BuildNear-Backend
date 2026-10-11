package com.buildnear.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceRequestDto {

    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerCity;

    private Long targetProfessionalId;
    private String targetProfessionalName;

    private String category;
    private String title;
    private String description;
    private String propertyType;
    private Double areaSqFt;
    private Double estimatedBudget;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private LocalDate preferredStartDate;
    private String urgency;
    private String status;
    private Integer quotationsCount;
    private List<QuotationDto> quotations;
    private LocalDateTime createdAt;

    public ServiceRequestDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public Long getTargetProfessionalId() {
        return targetProfessionalId;
    }

    public void setTargetProfessionalId(Long targetProfessionalId) {
        this.targetProfessionalId = targetProfessionalId;
    }

    public String getTargetProfessionalName() {
        return targetProfessionalName;
    }

    public void setTargetProfessionalName(String targetProfessionalName) {
        this.targetProfessionalName = targetProfessionalName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Double getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(Double areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public Double getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(Double estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDate getPreferredStartDate() {
        return preferredStartDate;
    }

    public void setPreferredStartDate(LocalDate preferredStartDate) {
        this.preferredStartDate = preferredStartDate;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuotationsCount() {
        return quotationsCount;
    }

    public void setQuotationsCount(Integer quotationsCount) {
        this.quotationsCount = quotationsCount;
    }

    public List<QuotationDto> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<QuotationDto> quotations) {
        this.quotations = quotations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
