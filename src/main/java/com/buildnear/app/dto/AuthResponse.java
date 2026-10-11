package com.buildnear.app.dto;

public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String fullName;
    private String role;
    private String city;
    private Long professionalProfileId;

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String email, String fullName, String role, String city, Long professionalProfileId) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.city = city;
        this.professionalProfileId = professionalProfileId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getProfessionalProfileId() {
        return professionalProfileId;
    }

    public void setProfessionalProfileId(Long professionalProfileId) {
        this.professionalProfileId = professionalProfileId;
    }
}
