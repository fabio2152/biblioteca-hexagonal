package pe.utec.biblioteca.infrastructure.out.persistence.memoria;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/** Adaptador de salida: guarda los socios en memoria. Activo cuando NO se usa el perfil "jpa". */
@Component
@Profile("!jpa")
public class MemoriaSocioRepository implements SocioRepository {

    private final Map<String, Socio> datos = new ConcurrentHashMap<>();

    @Override
    public Optional<Socio> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(datos.get(codigo));
    }

    @Override
    public List<Socio> listar() {
        return datos.values().stream().sorted(Comparator.comparing(Socio::getCodigo)).toList();
    }

    @Override
    public void guardar(Socio socio) {
        datos.put(socio.getCodigo(), socio);
    }
}
