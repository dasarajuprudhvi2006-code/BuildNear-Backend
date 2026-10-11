package com.buildnear.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "portfolio_items")
public class PortfolioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    @JsonIgnore
    private ProfessionalProfile professional;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 80)
    private String projectType;

    @Column(length = 100)
    private String location;

    @Column(length = 500)
    private String imageUrl;

    private Integer completedYear;

    public PortfolioItem() {}

    public PortfolioItem(ProfessionalProfile professional, String title, String description, String projectType, String location, String imageUrl, Integer completedYear) {
        this.professional = professional;
        this.title = title;
        this.description = description;
        this.projectType = projectType;
        this.location = location;
        this.imageUrl = imageUrl;
        this.completedYear = completedYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfessionalProfile getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalProfile professional) {
        this.professional = professional;
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

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCompletedYear() {
        return completedYear;
    }

    public void setCompletedYear(Integer completedYear) {
        this.completedYear = completedYear;
    }
}
