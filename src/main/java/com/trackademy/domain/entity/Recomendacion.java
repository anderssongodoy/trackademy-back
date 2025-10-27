package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "recomendacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recomendacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, columnDefinition = "text")
    private String texto;

    @Column(name = "fecha_sugerida")
    private LocalDate fechaSugerida;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;
}

