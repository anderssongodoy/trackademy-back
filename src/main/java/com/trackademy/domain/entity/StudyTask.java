package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "study_task")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudyTask {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha_sugerida")
    private LocalDate fechaSugerida;

    @Column(nullable = false)
    private Boolean completado = Boolean.FALSE;

    @Column
    private Integer semana;

    @Column(columnDefinition = "text")
    private String estado; // pendiente | en_progreso | hecho
}
