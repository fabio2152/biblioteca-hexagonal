package pe.utec.biblioteca.infrastructure.config;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.PoliticaPrestamo;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.out.LibroRepository;
import pe.utec.biblioteca.domain.port.out.PrestamoRepository;
import pe.utec.biblioteca.domain.port.out.SocioRepository;

/**
 * Carga datos de ejemplo para la demo (solo si no hay libros).
 * Usa los puertos de salida, así que funciona igual con memoria o con JPA.
 */
@Component
@Order(1)
public class DatosIniciales implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatosIniciales.class);

    private final LibroRepository libros;
    private final SocioRepository socios;
    private final PrestamoRepository prestamos;
    private final Clock reloj;

    public DatosIniciales(LibroRepository libros, SocioRepository socios, PrestamoRepository prestamos, Clock reloj) {
        this.libros = libros;
        this.socios = socios;
        this.prestamos = prestamos;
        this.reloj = reloj;
    }

    @Override
    public void run(String... args) {
        log.info(">>> Adaptador de persistencia activo: {}", libros.getClass().getSimpleName());
        if (!libros.listar().isEmpty()) {
            return;
        }
        LocalDate hoy = LocalDate.now(reloj);

        libros.guardar(new Libro("LIB-01", "Clean Code", "Robert C. Martin", false));
        libros.guardar(Libro.nuevo("LIB-02", "Clean Architecture", "Robert C. Martin"));
        libros.guardar(Libro.nuevo("LIB-03", "Design Patterns", "Gamma, Helm, Johnson y Vlissides"));
        libros.guardar(Libro.nuevo("LIB-04", "Head First Design Patterns", "Freeman y Robson"));
        libros.guardar(Libro.nuevo("LIB-05", "Fundamentals of Software Architecture", "Richards y Ford"));
        libros.guardar(Libro.nuevo("LIB-06", "Domain-Driven Design", "Eric Evans"));

        socios.guardar(Socio.nuevo("S001", "Ana Torres", "ana@utec.edu.pe"));
        socios.guardar(Socio.nuevo("S002", "Bruno Díaz", "bruno@utec.edu.pe"));
        socios.guardar(new Socio("S003", "Carla Ruiz", "carla@utec.edu.pe", new BigDecimal("5.00")));

        // Ana tiene "Clean Code" desde hace 20 días: ya venció y su devolución generará multa.
        LocalDate hace20Dias = hoy.minusDays(20);
        prestamos.guardar(Prestamo.reconstituir("demo-prestamo-vencido", "LIB-01", "S001", hace20Dias,
                hace20Dias.plusDays(PoliticaPrestamo.DIAS_DE_PRESTAMO), null, BigDecimal.ZERO));

        log.info(">>> Datos de ejemplo cargados: 6 libros, 3 socios y 1 préstamo vencido");
    }
}
