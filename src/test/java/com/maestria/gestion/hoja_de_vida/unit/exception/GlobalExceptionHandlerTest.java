package com.maestria.gestion.hoja_de_vida.unit.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import com.maestria.gestion.hoja_de_vida.exception.ApiError;
import com.maestria.gestion.hoja_de_vida.exception.ErrorCodes;
import com.maestria.gestion.hoja_de_vida.exception.GlobalExceptionHandler;

@DisplayName("Pruebas unitarias de GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Debe detallar los errores de validación de argumentos")
    void handleMethodArgumentNotValidExceptionDetallaCampoInvalido() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "valor", "El campo es obligatorio."));
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("metodoDePrueba", String.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(method, 0), bindingResult);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes/buscar");

        ResponseEntity<ApiError> response = handler.handleMethodArgumentNotValidException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).contains("valor: El campo es obligatorio.");
    }

    @Test
    @DisplayName("Debe detallar los errores de validación de formularios")
    void handleBindExceptionDetallaCampoInvalido() {
        BindException exception = new BindException(new Object(), "request");
        exception.addError(new FieldError("request", "fechaResolucion", "La fecha no puede ser futura."));
        MockHttpServletRequest request = new MockHttpServletRequest(
                "POST", "/api/hoja-vida/estudiantes/2024001/distinciones");

        ResponseEntity<ApiError> response = handler.handleBindException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).contains("fechaResolucion: La fecha no puede ser futura.");
    }

    @Test
    @DisplayName("Debe detallar el campo que incumple una restricción")
    void handleConstraintViolationExceptionDetallaCampoInvalido() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("buscar.valor");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("El parámetro es obligatorio.");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes/buscar");

        ResponseEntity<ApiError> response = handler.handleConstraintViolationException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).contains("valor: El parámetro es obligatorio.");
    }

    @Test
    @DisplayName("Debe retornar 413 cuando el archivo supera el tamaño permitido")
    void handleMaxUploadSizeExceededExceptionRetornaPayloadTooLarge() {
        MockHttpServletRequest request = new MockHttpServletRequest(
                "POST", "/api/hoja-vida/estudiantes/2024001/distinciones");

        ResponseEntity<ApiError> response = handler.handleMaxUploadSizeExceededException(
                new MaxUploadSizeExceededException(5L * 1024 * 1024),
                request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.PAYLOAD_TOO_LARGE);
    }

    @Test
    @DisplayName("Debe retornar 400 cuando el multipart es inválido")
    void handleMultipartExceptionRetornaBadRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest(
                "POST", "/api/hoja-vida/estudiantes/2024001/distinciones");

        ResponseEntity<ApiError> response = handler.handleMultipartException(
                new MultipartException("Contenido multipart inválido."),
                request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje()).contains("archivo adjunto");
    }

    @Test
    @DisplayName("Debe retornar 500 ante una excepción no controlada")
    void handleGenericExceptionRetornaInternalServerError() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes");

        ResponseEntity<ApiError> response = handler.handleGenericException(
                new RuntimeException("Error inesperado de prueba."),
                request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Debe usar un mensaje general cuando no se conoce el parámetro inválido")
    void handleMethodArgumentTypeMismatchExceptionSinNombreRetornaMensajeGeneral() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes/filtrar");
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "valor", Integer.class, null, null, new NumberFormatException());

        ResponseEntity<ApiError> response = handler.handleMethodArgumentTypeMismatchException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMensaje())
                .isEqualTo("Uno de los parámetros tiene un formato inválido.");
    }

    @SuppressWarnings("unused")
    private void metodoDePrueba(String valor) {
    }
}
