package pe.utec.biblioteca.domain.port.out;

import java.util.List;
import java.util.Optional;

import pe.utec.biblioteca.domain.model.Socio;

/** Puerto de salida: lo que el dominio necesita para guardar y leer socios. */
public interface SocioRepository {
    Optional<Socio> buscarPorCodigo(String codigo);
    List<Socio> listar();
    void guardar(Socio socio);
}
