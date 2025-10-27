package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String subject; // sub del JWT

    @Column(length = 200)
    private String email;

    @Column(columnDefinition = "text")
    private String nombre;

    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = java.time.OffsetDateTime.now();
        }
    }
}
