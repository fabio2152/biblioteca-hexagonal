package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;

/** Adaptador de salida: implementa el puerto PrestamoRepository con JPA. Activo con el perfil "jpa". */
@Component
@Profile("jpa")
public class JpaPrestamoRepository implements PrestamoRepository {

    private final SpringDataPrestamoRepository jpa;

    public JpaPrestamoRepository(SpringDataPrestamoRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void guardar(Prestamo prestamo) {
        jpa.save(PrestamoEntity.desdeDominio(prestamo));
    }

    @Override
    public Optional<Prestamo> buscarActivoPorLibro(String codigoLibro) {
        return jpa.findFirstByCodigoLibroAndFechaDevolucionIsNull(codigoLibro).map(PrestamoEntity::aDominio);
    }

    @Override
    public List<Prestamo> buscarPorSocio(String codigoSocio) {
        return jpa.findByCodigoSocioOrderByFechaPrestamoDesc(codigoSocio).stream().map(PrestamoEntity::aDominio).toList();
    }

    @Override
    public long contarActivosDeSocio(String codigoSocio) {
        return jpa.countByCodigoSocioAndFechaDevolucionIsNull(codigoSocio);
    }
}
