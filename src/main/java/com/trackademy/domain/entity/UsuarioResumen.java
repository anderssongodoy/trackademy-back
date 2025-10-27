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

    @Column(name = "promedio_ponderado", precision = 6, scale = 2)
    private BigDecimal promedioPonderado;

    @Column(name = "cursos_activos")
    private Integer cursosActivos;

    @Column(name = "creditos_totales")
    private Integer creditosTotales;

    @Column(name = "habitos_cumplidos_7d")
    private Integer habitosCumplidos7d;

    @Column(name = "riesgo_global_score", precision = 6, scale = 2)
    private BigDecimal riesgoGlobalScore;

    @Column(name = "proximo_recordatorio_fecha")
    private java.time.OffsetDateTime proximoRecordatorioFecha;
}
