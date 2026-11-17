package pe.utec.biblioteca.domain.exception;

/** Se lanza cuando se busca un libro, socio o préstamo que no existe. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
