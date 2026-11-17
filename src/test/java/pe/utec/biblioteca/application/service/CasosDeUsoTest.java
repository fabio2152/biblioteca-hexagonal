package pe.utec.biblioteca.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.exception.ReglaNegocioException;
import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.out.Notificador;
import pe.utec.biblioteca.infrastructure.out.persistence.memoria.MemoriaLibroRepository;
import pe.utec.biblioteca.infrastructure.out.persistence.memoria.MemoriaPrestamoRepository;
import pe.utec.biblioteca.infrastructure.out.persistence.memoria.MemoriaSocioRepository;

/**
 * Prueba los casos de uso con adaptadores EN MEMORIA y un reloj fijo:
 * no se levanta Spring, ni servidor, ni base de datos. Esto es lo que permite la arquitectura hexagonal.
 */
class CasosDeUsoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 11, 2);

    private MemoriaLibroRepository libros;
    private MemoriaSocioRepository socios;
    private MemoriaPrestamoRepository prestamos;
    private NotificadorDePrueba notificador;

    @BeforeEach
    void prepararDatos() {
        libros = new MemoriaLibroRepository();
        socios = new MemoriaSocioRepository();
        prestamos = new MemoriaPrestamoRepository();
        notificador = new NotificadorDePrueba();

        for (int i = 1; i <= 5; i++) {
            libros.guardar(Libro.nuevo("L" + i, "Libro " + i, "Autor"));
        }
        socios.guardar(Socio.nuevo("S1", "Ana", "ana@utec.edu.pe"));
        socios.guardar(new Socio("S2", "Carla", "carla@utec.edu.pe", new BigDecimal("3.00")));
    }

    @Test
    @DisplayName("Prestar marca el libro como no disponible y notifica al socio")
    void prestarLibro() {
        Prestamo prestamo = registrar(HOY).registrar("S1", "L1");

        assertThat(prestamo.getFechaVencimiento()).isEqualTo(HOY.plusDays(14));
        assertThat(libros.buscarPorCodigo("L1").orElseThrow().isDisponible()).isFalse();
        assertThat(notificador.mensajes).containsExactly("prestamo:S1:L1");
    }

    @Test
    @DisplayName("No se presta un libro que ya está prestado")
    void libroYaPrestado() {
        registrar(HOY).registrar("S1", "L1");

        assertThatThrownBy(() -> registrar(HOY).registrar("S1", "L1"))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    @DisplayName("El cuarto préstamo activo se rechaza")
    void cuartoPrestamoRechazado() {
        RegistrarPrestamoService servicio = registrar(HOY);
        servicio.registrar("S1", "L1");
        servicio.registrar("S1", "L2");
        servicio.registrar("S1", "L3");

        assertThatThrownBy(() -> servicio.registrar("S1", "L4"))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("máximo 3");
        assertThat(libros.buscarPorCodigo("L4").orElseThrow().isDisponible()).isTrue();
    }

    @Test
    @DisplayName("Un socio con multa no puede pedir libros hasta pagarla")
    void multaBloqueaHastaPagar() {
        assertThatThrownBy(() -> registrar(HOY).registrar("S2", "L1"))
                .isInstanceOf(ReglaNegocioException.class);

        BigDecimal pagado = new PagarMultaService(socios).pagar("S2");
        Prestamo prestamo = registrar(HOY).registrar("S2", "L1");

        assertThat(pagado).isEqualByComparingTo("3.00");
        assertThat(prestamo.estaActivo()).isTrue();
    }

    @Test
    @DisplayName("Devolver 6 días tarde suma S/ 6.00 de multa al socio")
    void devolucionTardia() {
        registrar(HOY).registrar("S1", "L1");

        Prestamo devuelto = devolver(HOY.plusDays(20)).devolver("L1");

        assertThat(devuelto.getMulta()).isEqualByComparingTo("6.00");
        assertThat(socios.buscarPorCodigo("S1").orElseThrow().getMultaPendiente()).isEqualByComparingTo("6.00");
        assertThat(libros.buscarPorCodigo("L1").orElseThrow().isDisponible()).isTrue();
        assertThat(notificador.mensajes).containsExactly("prestamo:S1:L1", "devolucion:S1:L1");
    }

    @Test
    @DisplayName("Socio o libro inexistente da error de no encontrado")
    void recursoInexistente() {
        assertThatThrownBy(() -> registrar(HOY).registrar("NO-EXISTE", "L1"))
                .isInstanceOf(RecursoNoEncontradoException.class);
        assertThatThrownBy(() -> registrar(HOY).registrar("S1", "NO-EXISTE"))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    // ---------- ayudantes ----------

    private RegistrarPrestamoService registrar(LocalDate fecha) {
        return new RegistrarPrestamoService(libros, socios, prestamos, notificador, relojEn(fecha));
    }

    private DevolverLibroService devolver(LocalDate fecha) {
        return new DevolverLibroService(libros, socios, prestamos, notificador, relojEn(fecha));
    }

    private static Clock relojEn(LocalDate fecha) {
        return Clock.fixed(fecha.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }

    /** Un adaptador de salida "falso" que solo recuerda qué se notificó. */
    private static class NotificadorDePrueba implements Notificador {
        final List<String> mensajes = new ArrayList<>();

        @Override
        public void prestamoRegistrado(Socio socio, Libro libro, Prestamo prestamo) {
            mensajes.add("prestamo:" + socio.getCodigo() + ":" + libro.getCodigo());
        }

        @Override
        public void libroDevuelto(Socio socio, Libro libro, Prestamo prestamo) {
            mensajes.add("devolucion:" + socio.getCodigo() + ":" + libro.getCodigo());
        }
    }
}
