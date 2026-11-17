package pe.utec.biblioteca.infrastructure.in.web.dto;

public record ErrorResponse(int estado, String error, String mensaje) {
}
