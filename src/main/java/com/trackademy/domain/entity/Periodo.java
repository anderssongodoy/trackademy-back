package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Periodo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
}

