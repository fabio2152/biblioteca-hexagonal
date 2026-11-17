package pe.utec.biblioteca.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record PrestamoRequest(@NotBlank String codigoSocio, @NotBlank String codigoLibro) {
}
