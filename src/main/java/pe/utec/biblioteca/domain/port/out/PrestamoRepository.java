package pe.utec.biblioteca.domain.port.out;

import java.util.List;
import java.util.Optional;

import pe.utec.biblioteca.domain.model.Prestamo;

/** Puerto de salida: lo que el dominio necesita para guardar y leer préstamos. */
public interface PrestamoRepository {
    void guardar(Prestamo prestamo);
    Optional<Prestamo> buscarActivoPorLibro(String codigoLibro);
    List<Prestamo> buscarPorSocio(String codigoSocio);
    long contarActivosDeSocio(String codigoSocio);
}
