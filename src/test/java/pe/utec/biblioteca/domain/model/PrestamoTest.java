package pe.utec.biblioteca.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.utec.biblioteca.domain.exception.ReglaNegocioException;

/** Pruebas del dominio puro: sin Spring, sin base de datos. */
class PrestamoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 11, 2);
    private final Libro libro = Libro.nuevo("L1", "Clean Code", "Robert C. Martin");
    private final Socio socio = Socio.nuevo("S1", "Ana", "ana@utec.edu.pe");

    @Test
    @DisplayName("Un préstamo vence a los 14 días")
    void venceALos14Dias() {
        Prestamo prestamo = Prestamo.registrar(libro, socio, HOY);

        assertThat(prestamo.getFechaVencimiento()).isEqualTo(HOY.plusDays(14));
        assertThat(prestamo.estaActivo()).isTrue();
    }

    @Test
    @DisplayName("Devolver a tiempo no genera multa")
    void devolucionATiempoSinMulta() {
        Prestamo prestamo = Prestamo.registrar(libro, socio, HOY);

        BigDecimal multa = prestamo.devolver(HOY.plusDays(14));

        assertThat(multa).isEqualByComparingTo("0");
        assertThat(prestamo.estaActivo()).isFalse();
    }

    @Test
    @DisplayName("Cada día de retraso cuesta S/ 1.00")
    void devolucionTardiaGeneraMulta() {
        Prestamo prestamo = Prestamo.registrar(libro, socio, HOY);

        BigDecimal multa = prestamo.devolver(HOY.plusDays(20));

        assertThat(multa).isEqualByComparingTo("6.00");
    }

    @Test
    @DisplayName("No se puede devolver dos veces el mismo préstamo")
    void noSeDevuelveDosVeces() {
        Prestamo prestamo = Prestamo.registrar(libro, socio, HOY);
        prestamo.devolver(HOY.plusDays(1));

        assertThatThrownBy(() -> prestamo.devolver(HOY.plusDays(2)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    @DisplayName("Un libro prestado no se puede volver a prestar")
    void libroPrestadoNoSePresta() {
        libro.prestar();

        assertThatThrownBy(libro::prestar)
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya está prestado");
    }
}
