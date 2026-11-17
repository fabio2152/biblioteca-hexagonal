package pe.utec.biblioteca.infrastructure.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.utec.biblioteca.application.service.ConsultarBibliotecaService;
import pe.utec.biblioteca.application.service.DevolverLibroService;
import pe.utec.biblioteca.application.service.PagarMultaService;
import pe.utec.biblioteca.application.service.RegistrarPrestamoService;
import pe.utec.biblioteca.domain.port.in.ConsultarBibliotecaUseCase;
import pe.utec.biblioteca.domain.port.in.DevolverLibroUseCase;
import pe.utec.biblioteca.domain.port.in.PagarMultaUseCase;
import pe.utec.biblioteca.domain.port.in.RegistrarPrestamoUseCase;
import pe.utec.biblioteca.domain.port.out.LibroRepository;
import pe.utec.biblioteca.domain.port.out.Notificador;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/**
 * Aquí se "enchufan" los adaptadores en los puertos.
 * Los casos de uso no tienen anotaciones de Spring: se crean a mano en este único lugar.
 * Spring inyecta el adaptador activo (memoria o JPA) según el perfil.
 */
@Configuration
public class CasosDeUsoConfig {

    @Bean
    public Clock reloj(@Value("${biblioteca.zona-horaria:America/Lima}") String zona) {
        return Clock.system(ZoneId.of(zona));
    }

    @Bean
    public RegistrarPrestamoUseCase registrarPrestamoUseCase(LibroRepository libros, SocioRepository socios,
                                                             PrestamoRepository prestamos, Notificador notificador,
                                                             Clock reloj) {
        return new RegistrarPrestamoService(libros, socios, prestamos, notificador, reloj);
    }

    @Bean
    public DevolverLibroUseCase devolverLibroUseCase(LibroRepository libros, SocioRepository socios,
                                                     PrestamoRepository prestamos, Notificador notificador,
                                                     Clock reloj) {
        return new DevolverLibroService(libros, socios, prestamos, notificador, reloj);
    }

    @Bean
    public PagarMultaUseCase pagarMultaUseCase(SocioRepository socios) {
        return new PagarMultaService(socios);
    }

    @Bean
    public ConsultarBibliotecaUseCase consultarBibliotecaUseCase(LibroRepository libros, SocioRepository socios,
                                                                 PrestamoRepository prestamos) {
        return new ConsultarBibliotecaService(libros, socios, prestamos);
    }
}
