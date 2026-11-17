package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.port.out.LibroRepository;

/** Adaptador de salida: implementa el puerto LibroRepository con JPA. Activo con el perfil "jpa". */
@Component
@Profile("jpa")
public class JpaLibroRepository implements LibroRepository {

    private final SpringDataLibroRepository jpa;

    public JpaLibroRepository(SpringDataLibroRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Libro> buscarPorCodigo(String codigo) {
        return jpa.findById(codigo).map(LibroEntity::aDominio);
    }

    @Override
    public List<Libro> listar() {
        return jpa.findAll(Sort.by("codigo")).stream().map(LibroEntity::aDominio).toList();
    }

    @Override
    public void guardar(Libro libro) {
        jpa.save(LibroEntity.desdeDominio(libro));
    }
}
