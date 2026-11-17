package pe.utec.biblioteca.infrastructure.out.notificacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;
import pe.utec.biblioteca.domain.port.out.Notificador;

/** Adaptador de salida: "envía" las notificaciones escribiéndolas en la consola. */
@Component
public class ConsolaNotificador implements Notificador {

    private static final Logger log = LoggerFactory.getLogger(ConsolaNotificador.class);

    @Override
    public void prestamoRegistrado(Socio socio, Libro libro, Prestamo prestamo) {
        log.info("[NOTIFICACIÓN a {}] Hola {}, te prestamos '{}'. Devuélvelo antes del {}.",
                socio.getEmail(), socio.getNombre(), libro.getTitulo(), prestamo.getFechaVencimiento());
    }

    @Override
    public void libroDevuelto(Socio socio, Libro libro, Prestamo prestamo) {
        if (prestamo.getMulta().signum() > 0) {
            log.info("[NOTIFICACIÓN a {}] Hola {}, recibimos '{}' con retraso. Multa: S/ {}.",
                    socio.getEmail(), socio.getNombre(), libro.getTitulo(), prestamo.getMulta());
        } else {
            log.info("[NOTIFICACIÓN a {}] Hola {}, recibimos '{}' a tiempo. ¡Gracias!",
                    socio.getEmail(), socio.getNombre(), libro.getTitulo());
        }
    }
}
