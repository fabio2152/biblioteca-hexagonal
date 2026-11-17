package pe.utec.biblioteca.domain.port.out;

import pe.utec.biblioteca.domain.model.Libro;
import pe.utec.biblioteca.domain.model.Prestamo;
import pe.utec.biblioteca.domain.model.Socio;

/** Puerto de salida: avisar al socio. Hoy es la consola; mañana podría ser correo o WhatsApp. */
public interface Notificador {
    void prestamoRegistrado(Socio socio, Libro libro, Prestamo prestamo);
    void libroDevuelto(Socio socio, Libro libro, Prestamo prestamo);
}
