package pe.utec.biblioteca.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import pe.utec.biblioteca.domain.exception.ReglaNegocioException;

public class Prestamo {

    private final String id;
    private final String codigoLibro;
    private final String codigoSocio;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    private BigDecimal multa;

    private Prestamo(String id, String codigoLibro, String codigoSocio, LocalDate fechaPrestamo,
                     LocalDate fechaVencimiento, LocalDate fechaDevolucion, BigDecimal multa) {
        this.id = id;
        this.codigoLibro = codigoLibro;
        this.codigoSocio = codigoSocio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.fechaDevolucion = fechaDevolucion;
        this.multa = (multa == null ? BigDecimal.ZERO : multa).setScale(2, RoundingMode.HALF_UP);
    }

    /** Crea un préstamo nuevo. Regla: vence a los {@link PoliticaPrestamo#DIAS_DE_PRESTAMO} días. */
    public static Prestamo registrar(Libro libro, Socio socio, LocalDate hoy) {
        return new Prestamo(UUID.randomUUID().toString(), libro.getCodigo(), socio.getCodigo(),
                hoy, hoy.plusDays(PoliticaPrestamo.DIAS_DE_PRESTAMO), null, BigDecimal.ZERO);
    }

    /** Reconstruye un préstamo ya guardado (lo usan los adaptadores de persistencia). */
    public static Prestamo reconstituir(String id, String codigoLibro, String codigoSocio, LocalDate fechaPrestamo,
                                        LocalDate fechaVencimiento, LocalDate fechaDevolucion, BigDecimal multa) {
        return new Prestamo(id, codigoLibro, codigoSocio, fechaPrestamo, fechaVencimiento, fechaDevolucion, multa);
    }

    /** Regla: cada día de retraso genera {@link PoliticaPrestamo#MULTA_POR_DIA}. Devuelve la multa generada. */
    public BigDecimal devolver(LocalDate hoy) {
        if (!estaActivo()) {
            throw new ReglaNegocioException("Este préstamo ya fue devuelto");
        }
        if (hoy.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior al préstamo");
        }
        fechaDevolucion = hoy;
        multa = PoliticaPrestamo.MULTA_POR_DIA
                .multiply(BigDecimal.valueOf(diasDeRetraso(hoy)))
                .setScale(2, RoundingMode.HALF_UP);
        return multa;
    }

    public long diasDeRetraso(LocalDate fecha) {
        return Math.max(0, ChronoUnit.DAYS.between(fechaVencimiento, fecha));
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public String getId() { return id; }
    public String getCodigoLibro() { return codigoLibro; }
    public String getCodigoSocio() { return codigoSocio; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public BigDecimal getMulta() { return multa; }
}
