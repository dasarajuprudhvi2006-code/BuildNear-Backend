package com.buildnear.app.dto;

import java.util.Map;

public class DashboardStatsDto {

    private long totalUsers;
    private long totalCustomers;
    private long totalProfessionals;
    private long verifiedProfessionals;
    private long pendingVerifications;

    private long totalBookings;
    private long activeBookings;
    private long completedBookings;
    private long cancelledBookings;

    private long totalServiceRequests;
    private long totalQuotations;
    private long totalReviews;
    private double averagePlatformRating;
    private double totalTransactionVolume;

    private Map<String, Long> professionalsByCategory;
    private Map<String, Long> bookingsByStatus;

    public DashboardStatsDto() {}

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalProfessionals() {
        return totalProfessionals;
    }

    public void setTotalProfessionals(long totalProfessionals) {
        this.totalProfessionals = totalProfessionals;
    }

    public long getVerifiedProfessionals() {
        return verifiedProfessionals;
    }

    public void setVerifiedProfessionals(long verifiedProfessionals) {
        this.verifiedProfessionals = verifiedProfessionals;
    }

    public long getPendingVerifications() {
        return pendingVerifications;
    }

    public void setPendingVerifications(long pendingVerifications) {
        this.pendingVerifications = pendingVerifications;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getActiveBookings() {
        return activeBookings;
    }

    public void setActiveBookings(long activeBookings) {
        this.activeBookings = activeBookings;
    }

    public long getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(long completedBookings) {
        this.completedBookings = completedBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }

    public long getTotalServiceRequests() {
        return totalServiceRequests;
    }

    public void setTotalServiceRequests(long totalServiceRequests) {
        this.totalServiceRequests = totalServiceRequests;
    }

    public long getTotalQuotations() {
        return totalQuotations;
    }

    public void setTotalQuotations(long totalQuotations) {
        this.totalQuotations = totalQuotations;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public double getAveragePlatformRating() {
        return averagePlatformRating;
    }

    public void setAveragePlatformRating(double averagePlatformRating) {
        this.averagePlatformRating = averagePlatformRating;
    }

    public double getTotalTransactionVolume() {
        return totalTransactionVolume;
    }

    public void setTotalTransactionVolume(double totalTransactionVolume) {
        this.totalTransactionVolume = totalTransactionVolume;
    }

    public Map<String, Long> getProfessionalsByCategory() {
        return professionalsByCategory;
    }

    public void setProfessionalsByCategory(Map<String, Long> professionalsByCategory) {
        this.professionalsByCategory = professionalsByCategory;
    }

    public Map<String, Long> getBookingsByStatus() {
        return bookingsByStatus;
    }

    public void setBookingsByStatus(Map<String, Long> bookingsByStatus) {
        this.bookingsByStatus = bookingsByStatus;
    }
}
