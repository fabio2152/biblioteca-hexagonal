package pe.utec.biblioteca.infrastructure.out.persistence.memoria;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.port.out.LibroRepository;

/** Adaptador de salida: guarda los libros en memoria. Activo cuando NO se usa el perfil "jpa". */
@Component
@Profile("!jpa")
public class MemoriaLibroRepository implements LibroRepository {

    private final Map<String, Libro> datos = new ConcurrentHashMap<>();

    @Override
    public Optional<Libro> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(datos.get(codigo));
    }

    @Override
    public List<Libro> listar() {
        return datos.values().stream().sorted(Comparator.comparing(Libro::getCodigo)).toList();
    }

    @Override
    public void guardar(Libro libro) {
        datos.put(libro.getCodigo(), libro);
    }
}
