package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/** Adaptador de salida: implementa el puerto SocioRepository con JPA. Activo con el perfil "jpa". */
@Component
@Profile("jpa")
public class JpaSocioRepository implements SocioRepository {

    private final SpringDataSocioRepository jpa;

    public JpaSocioRepository(SpringDataSocioRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Socio> buscarPorCodigo(String codigo) {
        return jpa.findById(codigo).map(SocioEntity::aDominio);
    }

    @Override
    public List<Socio> listar() {
        return jpa.findAll(Sort.by("codigo")).stream().map(SocioEntity::aDominio).toList();
    }

    @Override
    public void guardar(Socio socio) {
        jpa.save(SocioEntity.desdeDominio(socio));
    }
}
