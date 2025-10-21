package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "term")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;
    
    @Column(nullable = false)
    private String code;
    
    private String name;
    
    @Column(nullable = false)
    private Integer year;
    
    @Column(nullable = false)
    private Integer cycleNumber;
    
    private String periodRaw;
    
    private String fullName;
    
    @Column(nullable = false)
    private java.time.LocalDate startDate;
    
    @Column(nullable = false)
    private java.time.LocalDate endDate;
    
    private Boolean isActive;
}
