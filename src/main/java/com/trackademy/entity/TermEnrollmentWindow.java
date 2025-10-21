package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "term_enrollment_window")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermEnrollmentWindow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id")
    private Campus campus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;
    
    @Column(nullable = false)
    private LocalDate enrollmentStartDate;
    
    @Column(nullable = false)
    private LocalDate enrollmentEndDate;
    
    private LocalDate addDropDeadline;
    
    private Integer minCycleToEnroll;
    
    private Integer maxCycleToEnroll;
}
