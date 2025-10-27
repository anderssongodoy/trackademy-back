package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_habito")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioHabito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(length = 50)
    private String frecuencia; // diaria | semanal | personalizada

    @Column(name = "recordatorio_hora")
    private java.time.LocalTime recordatorioHora;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;
}
