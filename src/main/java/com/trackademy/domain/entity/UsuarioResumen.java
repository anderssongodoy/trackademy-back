package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "usuario_resumen")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioResumen {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "promedio_global", precision = 5, scale = 2)
    private BigDecimal promedioGlobal;

    @Column(name = "cursos_activos")
    private Integer cursosActivos;
}

