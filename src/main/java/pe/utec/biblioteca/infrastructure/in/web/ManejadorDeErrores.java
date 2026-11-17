package pe.utec.biblioteca.infrastructure.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pe.utec.biblioteca.domain.exception.RecursoNoEncontradoException;
import pe.utec.biblioteca.domain.exception.ReglaNegocioException;
import pe.utec.biblioteca.infrastructure.in.web.dto.ErrorResponse;

/** Traduce las excepciones del dominio a códigos HTTP. */
@RestControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaDeNegocio(ReglaNegocioException e) {
        return respuesta(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException e) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> datosInvalidos(MethodArgumentNotValidException e) {
        return respuesta(HttpStatus.BAD_REQUEST, "Faltan datos obligatorios en la petición");
    }

    private ResponseEntity<ErrorResponse> respuesta(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado)
                .body(new ErrorResponse(estado.value(), estado.getReasonPhrase(), mensaje));
    }
}
