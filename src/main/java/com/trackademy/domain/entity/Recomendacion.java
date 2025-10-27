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

    @Column(columnDefinition = "text")
    private String tipo; // estudio | organizacion | riesgo | bienestar

    @Column
    private Short prioridad; // 1..3

    @Column(columnDefinition = "text")
    private String mensaje;

    @Column(name = "data_json", columnDefinition = "text")
    private String dataJson;

    @Column(name = "creada_en")
    private java.time.OffsetDateTime creadaEn;

    @Column(name = "leida_en")
    private java.time.OffsetDateTime leidaEn;
}
