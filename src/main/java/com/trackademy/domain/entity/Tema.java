package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tema")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tema {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_id")
    private Unidad unidad;

    @Column(nullable = false, length = 200)
    private String titulo;
}

