package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curriculum_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumVersion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;
    
    @Column(nullable = false)
    private Integer versionNumber;
    
    private Integer academicYearStart;
    
    private String name;
    
    private String description;
    
    private Boolean isActive;
}
