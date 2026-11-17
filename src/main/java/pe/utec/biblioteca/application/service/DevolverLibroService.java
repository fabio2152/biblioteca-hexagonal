package pe.utec.biblioteca.application.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.exception.ReglaNegocioException;
import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.in.DevolverLibroUseCase;
import pe.utec.biblioteca.domain.port.out.LibroRepository;
import pe.utec.biblioteca.domain.port.out.Notificador;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/** Caso de uso: devolver un libro. Si llega tarde, la multa se suma al socio. */
public class DevolverLibroService implements DevolverLibroUseCase {

    private final LibroRepository libros;
    private final SocioRepository socios;
    private final PrestamoRepository prestamos;
    private final Notificador notificador;
    private final Clock reloj;

    public DevolverLibroService(LibroRepository libros, SocioRepository socios,
                                PrestamoRepository prestamos, Notificador notificador, Clock reloj) {
        this.libros = libros;
        this.socios = socios;
        this.prestamos = prestamos;
        this.notificador = notificador;
        this.reloj = reloj;
    }

    @Override
    public Prestamo devolver(String codigoLibro) {
        Libro libro = libros.buscarPorCodigo(codigoLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro " + codigoLibro));
        Prestamo prestamo = prestamos.buscarActivoPorLibro(codigoLibro)
                .orElseThrow(() -> new ReglaNegocioException("El libro '" + libro.getTitulo() + "' no está prestado"));
        Socio socio = socios.buscarPorCodigo(prestamo.getCodigoSocio())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + prestamo.getCodigoSocio()));

        BigDecimal multa = prestamo.devolver(LocalDate.now(reloj));
        libro.devolver();
        if (multa.signum() > 0) {
            socio.agregarMulta(multa);
        }

        libros.guardar(libro);
        prestamos.guardar(prestamo);
        socios.guardar(socio);
        notificador.libroDevuelto(socio, libro, prestamo);
        return prestamo;
    }
}
