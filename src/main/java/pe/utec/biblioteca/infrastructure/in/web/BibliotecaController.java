package pe.utec.biblioteca.infrastructure.in.web;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pe.utec.biblioteca.domain.port.in.ConsultarBibliotecaUseCase;
import pe.utec.biblioteca.domain.port.in.DevolverLibroUseCase;
import pe.utec.biblioteca.domain.port.in.PagarMultaUseCase;
import pe.utec.biblioteca.domain.port.in.RegistrarPrestamoUseCase;
import pe.utec.biblioteca.infrastructure.in.web.dto.DevolucionRequest;
import pe.utec.biblioteca.infrastructure.in.web.dto.LibroResponse;
import pe.utec.biblioteca.infrastructure.in.web.dto.PagoMultaResponse;
import pe.utec.biblioteca.infrastructure.in.web.dto.PrestamoRequest;
import pe.utec.biblioteca.infrastructure.in.web.dto.PrestamoResponse;
import pe.utec.biblioteca.infrastructure.in.web.dto.SocioResponse;

/**
 * Adaptador de entrada REST: traduce HTTP/JSON a llamadas a los puertos de entrada.
 * No contiene reglas de negocio y solo conoce las interfaces de los casos de uso.
 */
@RestController
@RequestMapping("/api")
public class BibliotecaController {

    private final RegistrarPrestamoUseCase registrarPrestamo;
    private final DevolverLibroUseCase devolverLibro;
    private final PagarMultaUseCase pagarMulta;
    private final ConsultarBibliotecaUseCase consultar;

    public BibliotecaController(RegistrarPrestamoUseCase registrarPrestamo, DevolverLibroUseCase devolverLibro,
                                PagarMultaUseCase pagarMulta, ConsultarBibliotecaUseCase consultar) {
        this.registrarPrestamo = registrarPrestamo;
        this.devolverLibro = devolverLibro;
        this.pagarMulta = pagarMulta;
        this.consultar = consultar;
    }

    @GetMapping("/libros")
    public List<LibroResponse> libros() {
        return consultar.listarLibros().stream().map(LibroResponse::de).toList();
    }

    @GetMapping("/socios")
    public List<SocioResponse> socios() {
        return consultar.listarSocios().stream().map(SocioResponse::de).toList();
    }

    @GetMapping("/socios/{codigo}/prestamos")
    public List<PrestamoResponse> prestamosDeSocio(@PathVariable String codigo) {
        return consultar.prestamosDeSocio(codigo).stream().map(PrestamoResponse::de).toList();
    }

    @PostMapping("/prestamos")
    @ResponseStatus(HttpStatus.CREATED)
    public PrestamoResponse prestar(@Valid @RequestBody PrestamoRequest request) {
        return PrestamoResponse.de(registrarPrestamo.registrar(request.codigoSocio(), request.codigoLibro()));
    }

    @PostMapping("/devoluciones")
    public PrestamoResponse devolver(@Valid @RequestBody DevolucionRequest request) {
        return PrestamoResponse.de(devolverLibro.devolver(request.codigoLibro()));
    }

    @PostMapping("/socios/{codigo}/pagos")
    public PagoMultaResponse pagar(@PathVariable String codigo) {
        return new PagoMultaResponse(codigo, pagarMulta.pagar(codigo));
    }
}
