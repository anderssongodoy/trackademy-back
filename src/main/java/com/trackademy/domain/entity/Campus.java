package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Campus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @Column(nullable = false, length = 120)
    private String nombre;
}

