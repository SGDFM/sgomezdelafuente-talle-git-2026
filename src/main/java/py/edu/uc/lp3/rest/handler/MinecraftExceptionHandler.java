package py.edu.uc.lp3.rest.handler;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import py.edu.uc.lp3.domain.minecraft.exception.EntidadNoEncontradaException;
import py.edu.uc.lp3.domain.minecraft.exception.EstadoEntidadInvalidoException;
import py.edu.uc.lp3.domain.minecraft.exception.ExplosionNoPermitidaException;
import py.edu.uc.lp3.rest.dto.ErrorResponse;

@RestControllerAdvice
public class MinecraftExceptionHandler {

	@ExceptionHandler(EntidadNoEncontradaException.class)
	public ResponseEntity<ErrorResponse> entidadNoEncontrada(
			EntidadNoEncontradaException exception, HttpServletRequest request) {
		return responder(HttpStatus.NOT_FOUND, exception, request);
	}

	@ExceptionHandler(EstadoEntidadInvalidoException.class)
	public ResponseEntity<ErrorResponse> estadoInvalido(
			EstadoEntidadInvalidoException exception, HttpServletRequest request) {
		return responder(HttpStatus.BAD_REQUEST, exception, request);
	}

	@ExceptionHandler(ExplosionNoPermitidaException.class)
	public ResponseEntity<ErrorResponse> explosionNoPermitida(
			ExplosionNoPermitidaException exception, HttpServletRequest request) {
		return responder(HttpStatus.CONFLICT, exception, request);
	}

	@ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<ErrorResponse> solicitudInvalida(Exception exception, HttpServletRequest request) {
		return responder(HttpStatus.BAD_REQUEST, exception, request);
	}

	private ResponseEntity<ErrorResponse> responder(
			HttpStatus status, Exception exception, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				Instant.now(), status.value(), exception.getClass().getSimpleName(), exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(body);
	}
}
