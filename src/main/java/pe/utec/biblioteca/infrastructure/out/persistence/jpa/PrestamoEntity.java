package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import pe.utec.biblioteca.domain.model.Prestamo;

/** Tabla "prestamos". */
@Entity
@Table(name = "prestamos")
public class PrestamoEntity {

    @Id
    private String id;
    @Column(nullable = false)
    private String codigoLibro;
    @Column(nullable = false)
    private String codigoSocio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;
    @Column(precision = 10, scale = 2)
    private BigDecimal multa;

    protected PrestamoEntity() {
    }

    static PrestamoEntity desdeDominio(Prestamo p) {
        PrestamoEntity e = new PrestamoEntity();
        e.id = p.getId();
        e.codigoLibro = p.getCodigoLibro();
        e.codigoSocio = p.getCodigoSocio();
        e.fechaPrestamo = p.getFechaPrestamo();
        e.fechaVencimiento = p.getFechaVencimiento();
        e.fechaDevolucion = p.getFechaDevolucion();
        e.multa = p.getMulta();
        return e;
    }

    Prestamo aDominio() {
        return Prestamo.reconstituir(id, codigoLibro, codigoSocio, fechaPrestamo, fechaVencimiento, fechaDevolucion, multa);
    }
}
