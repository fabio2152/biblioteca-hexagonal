package pe.utec.biblioteca.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record DevolucionRequest(@NotBlank String codigoLibro) {
}
