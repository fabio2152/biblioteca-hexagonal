package pe.utec.biblioteca.infrastructure.in.web.dto;

import java.math.BigDecimal;

public record PagoMultaResponse(String codigoSocio, BigDecimal montoPagado) {
}
