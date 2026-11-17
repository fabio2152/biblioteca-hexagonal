package pe.utec.biblioteca.domain.port.in;

import pe.utec.biblioteca.domain.model.Prestamo;

/** Puerto de entrada: devolver un libro prestado (puede generar multa). */
public interface DevolverLibroUseCase {
    Prestamo devolver(String codigoLibro);
}
