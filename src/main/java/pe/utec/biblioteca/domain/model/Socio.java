package pe.utec.biblioteca.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pe.utec.biblioteca.domain.exception.ReglaNegocioException;

public class Socio {

    private final String codigo;
    private final String nombre;
    private final String email;
    private BigDecimal multaPendiente;

    public Socio(String codigo, String nombre, String email, BigDecimal multaPendiente) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del socio es obligatorio");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
        this.multaPendiente = normalizar(multaPendiente == null ? BigDecimal.ZERO : multaPendiente);
    }

    public static Socio nuevo(String codigo, String nombre, String email) {
        return new Socio(codigo, nombre, email, BigDecimal.ZERO);
    }

    /**
     * Reglas: un socio con multa pendiente no puede pedir libros
     * y no puede tener más de {@link PoliticaPrestamo#MAX_PRESTAMOS_ACTIVOS} préstamos activos.
     */
    public void validarPuedePedirPrestamo(long prestamosActivos) {
        if (tieneMultaPendiente()) {
            throw new ReglaNegocioException(nombre + " tiene una multa pendiente de S/ "
                    + multaPendiente + " y no puede pedir libros");
        }
        if (prestamosActivos >= PoliticaPrestamo.MAX_PRESTAMOS_ACTIVOS) {
            throw new ReglaNegocioException(nombre + " ya tiene " + prestamosActivos
                    + " préstamos activos (máximo " + PoliticaPrestamo.MAX_PRESTAMOS_ACTIVOS + ")");
        }
    }

    public boolean tieneMultaPendiente() {
        return multaPendiente.signum() > 0;
    }

    public void agregarMulta(BigDecimal monto) {
        if (monto == null || monto.signum() < 0) {
            throw new IllegalArgumentException("La multa no puede ser negativa");
        }
        multaPendiente = normalizar(multaPendiente.add(monto));
    }

    /** Paga toda la multa pendiente y devuelve el monto pagado. */
    public BigDecimal pagarMulta() {
        if (!tieneMultaPendiente()) {
            throw new ReglaNegocioException(nombre + " no tiene multas pendientes");
        }
        BigDecimal pagado = multaPendiente;
        multaPendiente = normalizar(BigDecimal.ZERO);
        return pagado;
    }

    private static BigDecimal normalizar(BigDecimal monto) {
        return monto.setScale(2, RoundingMode.HALF_UP);
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public BigDecimal getMultaPendiente() { return multaPendiente; }
}
