package pe.utec.biblioteca.domain.model;

import pe.utec.biblioteca.domain.exception.ReglaNegocioException;

public class Libro {

    private final String codigo;
    private final String titulo;
    private final String autor;
    private boolean disponible;

    public Libro(String codigo, String titulo, String autor, boolean disponible) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del libro es obligatorio");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título del libro es obligatorio");
        }
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = disponible;
    }

    public static Libro nuevo(String codigo, String titulo, String autor) {
        return new Libro(codigo, titulo, autor, true);
    }

    /** Regla: un libro prestado no se puede volver a prestar. */
    public void prestar() {
        if (!disponible) {
            throw new ReglaNegocioException("El libro '" + titulo + "' ya está prestado");
        }
        disponible = false;
    }

    public void devolver() {
        disponible = true;
    }

    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean isDisponible() { return disponible; }
}
