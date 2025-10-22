package com.trackademy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "motivation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Motivation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String text;

    private Integer score; // user provided score or computed
    private LocalDateTime createdAt;
}
