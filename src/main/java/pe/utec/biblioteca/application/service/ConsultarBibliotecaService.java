package pe.utec.biblioteca.application.service;

import java.util.List;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.in.ConsultarBibliotecaUseCase;
import pe.utec.biblioteca.domain.port.out.LibroRepository;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/** Consultas de solo lectura sobre libros, socios y préstamos. */
public class ConsultarBibliotecaService implements ConsultarBibliotecaUseCase {

    private final LibroRepository libros;
    private final SocioRepository socios;
    private final PrestamoRepository prestamos;

    public ConsultarBibliotecaService(LibroRepository libros, SocioRepository socios, PrestamoRepository prestamos) {
        this.libros = libros;
        this.socios = socios;
        this.prestamos = prestamos;
    }

    @Override
    public List<Libro> listarLibros() {
        return libros.listar();
    }

    @Override
    public List<Socio> listarSocios() {
        return socios.listar();
    }

    @Override
    public List<Prestamo> prestamosDeSocio(String codigoSocio) {
        socios.buscarPorCodigo(codigoSocio)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + codigoSocio));
        return prestamos.buscarPorSocio(codigoSocio);
    }
}
