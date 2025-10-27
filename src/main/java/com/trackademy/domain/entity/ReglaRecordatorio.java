package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regla_recordatorio", uniqueConstraints = {
        @UniqueConstraint(name = "uk_regla_usuario_tipo", columnNames = {"usuario_id", "tipo"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReglaRecordatorio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "anticipacion_dias", nullable = false)
    private Integer anticipacionDias;

    @Column(nullable = false)
    private Boolean activo = Boolean.TRUE;

    @Column(columnDefinition = "text")
    private String tipo; // evaluacion | habito | agenda

    @Column(columnDefinition = "text")
    private String canal; // app | email | sms
}
