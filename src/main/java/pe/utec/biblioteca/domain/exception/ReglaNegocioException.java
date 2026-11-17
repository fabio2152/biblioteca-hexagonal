package pe.utec.biblioteca.domain.exception;

/** Se lanza cuando una operación viola una regla del negocio de la biblioteca. */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
