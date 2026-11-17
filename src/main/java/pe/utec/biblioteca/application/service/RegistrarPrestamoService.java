package pe.utec.biblioteca.application.service;

import java.time.Clock;
import java.time.LocalDate;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.in.RegistrarPrestamoUseCase;
import pe.utec.biblioteca.domain.port.out.LibroRepository;
import pe.utec.biblioteca.domain.port.out.Notificador;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/**
 * Caso de uso: prestar un libro.
 * Es Java puro: no conoce Spring, HTTP ni la base de datos; solo habla con puertos.
 */
public class RegistrarPrestamoService implements RegistrarPrestamoUseCase {

    private final LibroRepository libros;
    private final SocioRepository socios;
    private final PrestamoRepository prestamos;
    private final Notificador notificador;
    private final Clock reloj;

    public RegistrarPrestamoService(LibroRepository libros, SocioRepository socios,
                                    PrestamoRepository prestamos, Notificador notificador, Clock reloj) {
        this.libros = libros;
        this.socios = socios;
        this.prestamos = prestamos;
        this.notificador = notificador;
        this.reloj = reloj;
    }

    @Override
    public Prestamo registrar(String codigoSocio, String codigoLibro) {
        Socio socio = socios.buscarPorCodigo(codigoSocio)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio " + codigoSocio));
        Libro libro = libros.buscarPorCodigo(codigoLibro)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el libro " + codigoLibro));

        socio.validarPuedePedirPrestamo(prestamos.contarActivosDeSocio(socio.getCodigo()));
        libro.prestar();
        Prestamo prestamo = Prestamo.registrar(libro, socio, LocalDate.now(reloj));

        libros.guardar(libro);
        prestamos.guardar(prestamo);
        notificador.prestamoRegistrado(socio, libro, prestamo);
        return prestamo;
    }
}
