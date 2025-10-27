package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidad", uniqueConstraints = {
        @UniqueConstraint(name = "uk_unidad_curso_numero", columnNames = {"curso_id", "nro"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Unidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(name = "nro", nullable = false)
    private Integer nro;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "semana_inicio")
    private Integer semanaInicio;

    @Column(name = "semana_fin")
    private Integer semanaFin;

    @Column(name = "logro_especifico", columnDefinition = "text")
    private String logroEspecifico;
}
