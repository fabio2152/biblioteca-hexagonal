package pe.utec.biblioteca.infrastructure.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import pe.utec.biblioteca.domain.model.Prestamo;

public record PrestamoResponse(String id, String codigoLibro, String codigoSocio, LocalDate fechaPrestamo,
                               LocalDate fechaVencimiento, LocalDate fechaDevolucion, BigDecimal multa,
                               String estado) {

    public static PrestamoResponse de(Prestamo p) {
        return new PrestamoResponse(p.getId(), p.getCodigoLibro(), p.getCodigoSocio(), p.getFechaPrestamo(),
                p.getFechaVencimiento(), p.getFechaDevolucion(), p.getMulta(),
                p.estaActivo() ? "ACTIVO" : "DEVUELTO");
    }
}
