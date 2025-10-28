package com.trackademy.dto;

import jakarta.validation.constraints.NotBlank;

public record AvatarRequest(@NotBlank String image) {}

