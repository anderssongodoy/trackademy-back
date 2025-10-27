package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competencia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_comp_univ_tipo_nombre", columnNames = {"universidad_id", "tipo", "nombre"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Competencia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "universidad_id", nullable = false)
    private Universidad universidad;

    @Column(columnDefinition = "text", nullable = false)
    private String tipo; // general | especifica
}
