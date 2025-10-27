package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_habito_log", uniqueConstraints = {
        @UniqueConstraint(name = "uk_habito_fecha", columnNames = {"usuario_habito_id", "fecha"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UsuarioHabitoLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_habito_id")
    private UsuarioHabito usuarioHabito;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Boolean cumplido = Boolean.FALSE;

    @Column(columnDefinition = "text")
    private String nota;
}
