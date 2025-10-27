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
    private String nombre;

    @Column(length = 50)
    private String periodicidad; // p.ej. DIARIO, SEMANAL

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;
}

