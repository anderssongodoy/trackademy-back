package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;
    
    private String studentCode;
    
    @Column(nullable = false)
    private Integer currentCycle;
    
    @Column(nullable = false)
    private Integer entryCycle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expected_graduation_term_id")
    private Term expectedGraduationTerm;
    
    private String status;
    
    private LocalDate enrollmentDate;
    
    private LocalDate expectedGraduationDate;
    
    private BigDecimal gpa;
    
    private Integer totalCreditsCompleted;
    
    private Integer totalCreditsInProgress;
}
