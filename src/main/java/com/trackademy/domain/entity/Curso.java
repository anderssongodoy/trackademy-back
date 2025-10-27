package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_curso_univ_codigo", columnNames = {"universidad_id", "codigo"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Curso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @Column(nullable = false, length = 40)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(name = "horas_semanales")
    private Integer horasSemanales;
}

