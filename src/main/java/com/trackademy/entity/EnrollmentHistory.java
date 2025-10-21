package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;
    
    @Column(nullable = false)
    private String action;
    
    private String previousStatus;
    
    private String newStatus;
    
    private Long changedBy;
    
    private String changeReason;
    
    private LocalDateTime changedAt;
}
