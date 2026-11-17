package pe.utec.biblioteca.domain.model;

import java.math.BigDecimal;

/** Reglas numéricas de la biblioteca, en un solo lugar. */
public final class PoliticaPrestamo {

    public static final int MAX_PRESTAMOS_ACTIVOS = 3;
    public static final int DIAS_DE_PRESTAMO = 14;
    public static final BigDecimal MULTA_POR_DIA = new BigDecimal("1.00");

    private PoliticaPrestamo() {
    }
}
