package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import pe.utec.biblioteca.domain.model.Libro;

/** Tabla "libros". Es un detalle de infraestructura: el dominio nunca ve esta clase. */
@Entity
@Table(name = "libros")
public class LibroEntity {

    @Id
    private String codigo;
    @Column(nullable = false)
    private String titulo;
    private String autor;
    private boolean disponible;

    protected LibroEntity() {
    }

    static LibroEntity desdeDominio(Libro libro) {
        LibroEntity e = new LibroEntity();
        e.codigo = libro.getCodigo();
        e.titulo = libro.getTitulo();
        e.autor = libro.getAutor();
        e.disponible = libro.isDisponible();
        return e;
    }

    Libro aDominio() {
        return new Libro(codigo, titulo, autor, disponible);
    }
}
