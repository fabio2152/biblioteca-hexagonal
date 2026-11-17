package pe.utec.biblioteca.domain.port.in;

import java.math.BigDecimal;

/** Puerto de entrada: pagar la multa pendiente de un socio. */
public interface PagarMultaUseCase {
    BigDecimal pagar(String codigoSocio);
}
