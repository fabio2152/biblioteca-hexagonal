package pe.utec.biblioteca.infrastructure.in.cli;

import java.io.PrintStream;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.exception.ReglaNegocioException;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.port.in.ConsultarBibliotecaUseCase;
import pe.utec.biblioteca.domain.port.in.DevolverLibroUseCase;
import pe.utec.biblioteca.domain.port.in.PagarMultaUseCase;
import pe.utec.biblioteca.domain.port.in.RegistrarPrestamoUseCase;

/**
 * Adaptador de entrada por línea de comandos.
 * Usa EXACTAMENTE los mismos casos de uso que el controlador REST.
 * Se activa con el perfil "cli".
 */
@Component
@Profile("cli")
@Order(2)
public class BibliotecaCli implements CommandLineRunner {

    private final RegistrarPrestamoUseCase registrarPrestamo;
    private final DevolverLibroUseCase devolverLibro;
    private final PagarMultaUseCase pagarMulta;
    private final ConsultarBibliotecaUseCase consultar;
    private final PrintStream out = System.out;

    public BibliotecaCli(RegistrarPrestamoUseCase registrarPrestamo, DevolverLibroUseCase devolverLibro,
                         PagarMultaUseCase pagarMulta, ConsultarBibliotecaUseCase consultar) {
        this.registrarPrestamo = registrarPrestamo;
        this.devolverLibro = devolverLibro;
        this.pagarMulta = pagarMulta;
        this.consultar = consultar;
    }

    @Override
    public void run(String... args) {
        ayuda();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            out.print("\nbiblioteca> ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String[] partes = scanner.nextLine().trim().split("\\s+");
            try {
                switch (partes[0].toLowerCase()) {
                    case "libros" -> consultar.listarLibros().forEach(l -> out.printf("  %-7s %-40s %s%n",
                            l.getCodigo(), l.getTitulo(), l.isDisponible() ? "disponible" : "PRESTADO"));
                    case "socios" -> consultar.listarSocios().forEach(s -> out.printf("  %-5s %-15s multa: S/ %s%n",
                            s.getCodigo(), s.getNombre(), s.getMultaPendiente()));
                    case "prestar" -> {
                        Prestamo p = registrarPrestamo.registrar(arg(partes, 1), arg(partes, 2));
                        out.println("  OK: préstamo registrado, vence el " + p.getFechaVencimiento());
                    }
                    case "devolver" -> {
                        Prestamo p = devolverLibro.devolver(arg(partes, 1));
                        out.println("  OK: libro devuelto. Multa: S/ " + p.getMulta());
                    }
                    case "prestamos" -> consultar.prestamosDeSocio(arg(partes, 1)).forEach(p -> out.printf(
                            "  %-7s prestado %s  vence %s  %s%n", p.getCodigoLibro(), p.getFechaPrestamo(),
                            p.getFechaVencimiento(), p.estaActivo() ? "ACTIVO" : "devuelto (multa S/ " + p.getMulta() + ")"));
                    case "pagar" -> out.println("  OK: se pagaron S/ " + pagarMulta.pagar(arg(partes, 1)));
                    case "ayuda", "" -> ayuda();
                    case "salir" -> {
                        return;
                    }
                    default -> out.println("  Comando no reconocido. Escribe 'ayuda'.");
                }
            } catch (ReglaNegocioException | RecursoNoEncontradoException | IllegalArgumentException e) {
                out.println("  NO PERMITIDO: " + e.getMessage());
            }
        }
    }

    private static String arg(String[] partes, int i) {
        if (partes.length <= i) {
            throw new IllegalArgumentException("Faltan argumentos. Escribe 'ayuda'.");
        }
        return partes[i];
    }

    private void ayuda() {
        out.println("""

                ===== Biblioteca (adaptador de línea de comandos) =====
                  libros                       lista los libros
                  socios                       lista los socios
                  prestar <socio> <libro>      ej.: prestar S002 LIB-02
                  devolver <libro>             ej.: devolver LIB-01
                  prestamos <socio>            préstamos de un socio
                  pagar <socio>                paga la multa pendiente
                  salir""");
    }
}
