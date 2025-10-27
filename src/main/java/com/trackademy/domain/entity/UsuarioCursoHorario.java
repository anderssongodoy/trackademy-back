package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "usuario_curso_horario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioCursoHorario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_curso_id")
    private UsuarioCurso usuarioCurso;

    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana; // 1(Lunes)..7(Domingo)

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "duracion_min", nullable = false)
    private Integer duracionMin; // 45, 90, 135, 180
}

