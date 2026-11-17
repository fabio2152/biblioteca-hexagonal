package pe.utec.biblioteca.domain.port.out;

import java.util.List;
import java.util.Optional;

import pe.utec.biblioteca.domain.model.Libro;

/** Puerto de salida: lo que el dominio necesita para guardar y leer libros. */
public interface LibroRepository {
    Optional<Libro> buscarPorCodigo(String codigo);
    List<Libro> listar();
    void guardar(Libro libro);
}
