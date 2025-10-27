package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competencia")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Competencia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;
}

