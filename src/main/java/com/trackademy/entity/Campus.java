package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String region;
    
    private String address;
    
    private Boolean active;
}
