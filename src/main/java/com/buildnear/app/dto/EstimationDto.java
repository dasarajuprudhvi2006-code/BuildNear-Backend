package com.buildnear.app.dto;

import java.util.Map;

public class EstimationDto {

    // Request fields
    private String estimationType; // HOUSE_CONSTRUCTION, INTERIOR_DESIGN
    private Double builtUpAreaSqFt;
    private Integer floorsCount;
    private String packageTier; // BASIC, STANDARD, LUXURY
    private String homeType; // 1BHK, 2BHK, 3BHK, 4BHK_VILLA
    private String interiorScope; // MODULAR_KITCHEN, ESSENTIALS, FULL_HOME, LUXURY
    private String city;

    // Response fields
    private Double ratePerUnit;
    private String unitName;
    private Double totalEstimatedCost;
    private String estimatedTimeline;
    private String disclaimer;
    private Map<String, Double> costBreakdown;
    private Map<String, String> itemizedDetails;

    public EstimationDto() {}

    public String getEstimationType() {
        return estimationType;
    }

    public void setEstimationType(String estimationType) {
        this.estimationType = estimationType;
    }

    public Double getBuiltUpAreaSqFt() {
        return builtUpAreaSqFt;
    }

    public void setBuiltUpAreaSqFt(Double builtUpAreaSqFt) {
        this.builtUpAreaSqFt = builtUpAreaSqFt;
    }

    public Integer getFloorsCount() {
        return floorsCount;
    }

    public void setFloorsCount(Integer floorsCount) {
        this.floorsCount = floorsCount;
    }

    public String getPackageTier() {
        return packageTier;
    }

    public void setPackageTier(String packageTier) {
        this.packageTier = packageTier;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getInteriorScope() {
        return interiorScope;
    }

    public void setInteriorScope(String interiorScope) {
        this.interiorScope = interiorScope;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Double ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    public void setTotalEstimatedCost(Double totalEstimatedCost) {
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public String getEstimatedTimeline() {
        return estimatedTimeline;
    }

    public void setEstimatedTimeline(String estimatedTimeline) {
        this.estimatedTimeline = estimatedTimeline;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Map<String, Double> getCostBreakdown() {
        return costBreakdown;
    }

    public void setCostBreakdown(Map<String, Double> costBreakdown) {
        this.costBreakdown = costBreakdown;
    }

    public Map<String, String> getItemizedDetails() {
        return itemizedDetails;
    }

    public void setItemizedDetails(Map<String, String> itemizedDetails) {
        this.itemizedDetails = itemizedDetails;
    }
}
