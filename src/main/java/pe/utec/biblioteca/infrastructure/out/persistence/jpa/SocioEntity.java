package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import pe.utec.biblioteca.domain.model.Socio;

/** Tabla "socios". */
@Entity
@Table(name = "socios")
public class SocioEntity {

    @Id
    private String codigo;
    @Column(nullable = false)
    private String nombre;
    private String email;
    @Column(precision = 10, scale = 2)
    private BigDecimal multaPendiente;

    protected SocioEntity() {
    }

    static SocioEntity desdeDominio(Socio socio) {
        SocioEntity e = new SocioEntity();
        e.codigo = socio.getCodigo();
        e.nombre = socio.getNombre();
        e.email = socio.getEmail();
        e.multaPendiente = socio.getMultaPendiente();
        return e;
    }

    Socio aDominio() {
        return new Socio(codigo, nombre, email, multaPendiente);
    }
}
