package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPrestamoRepository extends JpaRepository<PrestamoEntity, String> {

    Optional<PrestamoEntity> findFirstByCodigoLibroAndFechaDevolucionIsNull(String codigoLibro);

    List<PrestamoEntity> findByCodigoSocioOrderByFechaPrestamoDesc(String codigoSocio);

    long countByCodigoSocioAndFechaDevolucionIsNull(String codigoSocio);
}
