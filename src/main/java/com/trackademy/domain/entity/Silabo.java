package com.trackademy.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "silabo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Silabo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso; // permite versionado 1..N

    @Column(columnDefinition = "text")
    private String version;

    @Column
    private Boolean vigente = Boolean.TRUE;

    @Column(name = "fuente_pdf", columnDefinition = "text")
    private String fuentePdf;

    @Column(name = "hash_pdf", columnDefinition = "text")
    private String hashPdf;

    @Column(columnDefinition = "text")
    private String sumilla;

    @Column(columnDefinition = "text")
    private String fundamentacion;

    @Column(columnDefinition = "text")
    private String metodologia;

    @Column(name = "logro_general", columnDefinition = "text")
    private String logroGeneral;
}
