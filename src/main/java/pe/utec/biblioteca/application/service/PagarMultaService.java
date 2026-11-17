package pe.utec.biblioteca.application.service;

import java.math.BigDecimal;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.in.PagarMultaUseCase;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/** Caso de uso: pagar la multa pendiente para volver a pedir libros. */
public class PagarMultaService implements PagarMultaUseCase {

    private final SocioRepository socios;

    public PagarMultaService(SocioRepository socios) {
        this.socios = socios;
    }

    @Override
    public BigDecimal pagar(String codigoSocio) {
        Socio socio = socios.buscarPorCodigo(codigoSocio)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + codigoSocio));
        BigDecimal pagado = socio.pagarMulta();
        socios.guardar(socio);
        return pagado;
    }
}
