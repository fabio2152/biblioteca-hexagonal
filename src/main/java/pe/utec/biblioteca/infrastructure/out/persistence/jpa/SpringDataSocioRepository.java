package pe.utec.biblioteca.infrastructure.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSocioRepository extends JpaRepository<SocioEntity, String> {
}
