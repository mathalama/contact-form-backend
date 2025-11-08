package dev.mathallama.dto;

import jakarta.validation.constraints.*;

public record ContactRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank @Size(min=1, max=2000) String message
) {}
