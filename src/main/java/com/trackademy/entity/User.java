package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String name;
    
    private String picture;
    
    @Column(nullable = false)
    private String provider;
    
    @Column(nullable = false, unique = true)
    private String externalId;
    
    @Column(name = "provider_subject")
    private String providerSubject;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "preferred_campus_id")
    private Campus preferredCampus;

    private Integer preferredCycle;

    @Column(name = "program_id")
    private String preferredProgram;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "career_interests", columnDefinition = "TEXT")
    private String careerInterests;

    @Column(name = "study_hours_per_day")
    private Integer studyHoursPerDay;

    @Column(name = "learning_style")
    private String learningStyle;

    @Column(name = "motivation_factors", columnDefinition = "TEXT")
    private String motivationFactors;

    @Column(name = "wants_alerts")
    private Boolean wantsAlerts;

    @Column(name = "wants_incentives")
    private Boolean wantsIncentives;

    @Column(name = "allow_data_sharing")
    private Boolean allowDataSharing;

    @Column(name = "onboarded")
    private Boolean onboarded;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
