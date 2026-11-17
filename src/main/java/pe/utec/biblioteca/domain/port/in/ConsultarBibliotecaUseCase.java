package pe.utec.biblioteca.domain.port.in;

import java.util.List;

import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;

/** Puerto de entrada: consultas de solo lectura. */
public interface ConsultarBibliotecaUseCase {
    List<Libro> listarLibros();
    List<Socio> listarSocios();
    List<Prestamo> prestamosDeSocio(String codigoSocio);
}
