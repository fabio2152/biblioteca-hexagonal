package pe.utec.biblioteca.infrastructure.out.persistence.memoria;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;

/** Adaptador de salida: guarda los préstamos en memoria. Activo cuando NO se usa el perfil "jpa". */
@Component
@Profile("!jpa")
public class MemoriaPrestamoRepository implements PrestamoRepository {

    private final Map<String, Prestamo> datos = new ConcurrentHashMap<>();

    @Override
    public void guardar(Prestamo prestamo) {
        datos.put(prestamo.getId(), prestamo);
    }

    @Override
    public Optional<Prestamo> buscarActivoPorLibro(String codigoLibro) {
        return datos.values().stream()
                .filter(p -> p.getCodigoLibro().equals(codigoLibro) && p.estaActivo())
                .findFirst();
    }

    @Override
    public List<Prestamo> buscarPorSocio(String codigoSocio) {
        return datos.values().stream()
                .filter(p -> p.getCodigoSocio().equals(codigoSocio))
                .sorted(Comparator.comparing(Prestamo::getFechaPrestamo).reversed())
                .toList();
    }

    @Override
    public long contarActivosDeSocio(String codigoSocio) {
        return datos.values().stream()
                .filter(p -> p.getCodigoSocio().equals(codigoSocio) && p.estaActivo())
                .count();
    }
}
