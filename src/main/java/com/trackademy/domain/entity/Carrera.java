package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrera", uniqueConstraints = {
        @UniqueConstraint(name = "uk_carrera_univ_nombre", columnNames = {"universidad_id", "nombre"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Carrera {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @Column(nullable = false, length = 150)
    private String nombre;
}

