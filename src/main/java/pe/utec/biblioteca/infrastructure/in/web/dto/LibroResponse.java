package pe.utec.biblioteca.infrastructure.in.web.dto;

import pe.utec.biblioteca.domain.model.Libro;

public record LibroResponse(String codigo, String titulo, String autor, boolean disponible) {

    public static LibroResponse de(Libro libro) {
        return new LibroResponse(libro.getCodigo(), libro.getTitulo(), libro.getAutor(), libro.isDisponible());
    }
}
