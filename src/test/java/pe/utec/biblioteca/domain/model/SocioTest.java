package pe.utec.biblioteca.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pe.utec.biblioteca.domain.exception.ReglaNegocioException;

class SocioTest {

    @Test
    @DisplayName("Un socio sin multa y con menos de 3 préstamos puede pedir libros")
    void puedePedir() {
        Socio socio = Socio.nuevo("S1", "Ana", "ana@utec.edu.pe");

        assertThatCode(() -> socio.validarPuedePedirPrestamo(2)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Máximo 3 préstamos activos")
    void maximoTresPrestamos() {
        Socio socio = Socio.nuevo("S1", "Ana", "ana@utec.edu.pe");

        assertThatThrownBy(() -> socio.validarPuedePedirPrestamo(3))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("máximo 3");
    }

    @Test
    @DisplayName("Un socio con multa pendiente no puede pedir libros")
    void conMultaNoPuedePedir() {
        Socio socio = new Socio("S1", "Ana", "ana@utec.edu.pe", new BigDecimal("2.00"));

        assertThatThrownBy(() -> socio.validarPuedePedirPrestamo(0))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("multa");
    }

    @Test
    @DisplayName("Pagar la multa la deja en cero")
    void pagarMulta() {
        Socio socio = Socio.nuevo("S1", "Ana", "ana@utec.edu.pe");
        socio.agregarMulta(new BigDecimal("4.00"));

        BigDecimal pagado = socio.pagarMulta();

        assertThat(pagado).isEqualByComparingTo("4.00");
        assertThat(socio.tieneMultaPendiente()).isFalse();
    }
}
