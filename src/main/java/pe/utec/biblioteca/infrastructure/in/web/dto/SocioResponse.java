package pe.utec.biblioteca.infrastructure.in.web.dto;

import java.math.BigDecimal;

import pe.utec.biblioteca.domain.model.Socio;

public record SocioResponse(String codigo, String nombre, String email, BigDecimal multaPendiente) {

    public static SocioResponse de(Socio socio) {
        return new SocioResponse(socio.getCodigo(), socio.getNombre(), socio.getEmail(), socio.getMultaPendiente());
    }
}
