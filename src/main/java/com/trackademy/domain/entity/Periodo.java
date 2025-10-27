package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodo",
        uniqueConstraints = {@UniqueConstraint(name = "uk_periodo_univ_etiqueta", columnNames = {"universidad_id", "etiqueta"})})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Periodo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @Column(name = "etiqueta", nullable = false, columnDefinition = "text")
    private String etiqueta;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}
