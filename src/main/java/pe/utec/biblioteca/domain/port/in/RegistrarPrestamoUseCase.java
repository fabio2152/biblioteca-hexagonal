package pe.utec.biblioteca.domain.port.in;

import pe.utec.biblioteca.domain.model.Prestamo;

/** Puerto de entrada: prestar un libro a un socio. */
public interface RegistrarPrestamoUseCase {
    Prestamo registrar(String codigoSocio, String codigoLibro);
}
