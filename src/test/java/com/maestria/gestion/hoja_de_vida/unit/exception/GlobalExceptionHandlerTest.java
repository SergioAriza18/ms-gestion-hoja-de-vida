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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.maestria.gestion.hoja_de_vida.exception.ApiError;
import com.maestria.gestion.hoja_de_vida.exception.ErrorCodes;
import com.maestria.gestion.hoja_de_vida.exception.GlobalExceptionHandler;
import com.maestria.gestion.hoja_de_vida.exception.ResourceNotFoundException;

@DisplayName("Pruebas unitarias de GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Debe retornar ApiError con estado 400 cuando falla la validación de un argumento")
    void handleMethodArgumentNotValidExceptionRetornaApiErrorConEstado400() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "valor", "El campo es obligatorio."));
        Method method = GlobalExceptionHandlerTest.class.getDeclaredMethod("metodoDePrueba", String.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(method, 0), bindingResult);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes/buscar");

        ResponseEntity<ApiError> response = handler.handleMethodArgumentNotValidException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.BAD_REQUEST);
        assertThat(response.getBody().getMensaje()).contains("valor: El campo es obligatorio.");
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 400 cuando falla una restricción de parámetros")
    void handleConstraintViolationExceptionRetornaApiErrorConEstado400() {
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
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.BAD_REQUEST);
        assertThat(response.getBody().getMensaje()).contains("valor: El parámetro es obligatorio.");
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 404 cuando no existe la ruta solicitada")
    void handleNoHandlerFoundExceptionRetornaApiErrorConEstado404() {
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/api/no-existe", new HttpHeaders());
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/no-existe");

        ResponseEntity<ApiError> response = handler.handleNoHandlerFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.RESOURCE_NOT_FOUND);
        assertThat(response.getBody().getUrl()).isEqualTo("/api/no-existe");
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 405 cuando el método HTTP no está soportado")
    void handleHttpRequestMethodNotSupportedExceptionRetornaApiErrorConEstado405() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/hoja-vida/estudiantes");

        ResponseEntity<ApiError> response = handler.handleHttpRequestMethodNotSupportedException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.METHOD_NOT_ALLOWED);
        assertThat(response.getBody().getMensaje()).contains("POST");
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 404 cuando el recurso no existe")
    void handleResourceNotFoundExceptionRetornaApiErrorConEstado404() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET",
                "/api/hoja-vida/estudiantes/NO-EXISTE/historia-academica");
        ResourceNotFoundException exception = new ResourceNotFoundException("No se encontró el recurso.");

        ResponseEntity<ApiError> response = handler.handleResourceNotFoundException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEstado()).isEqualTo(404);
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.RESOURCE_NOT_FOUND);
        assertThat(response.getBody().getMensaje()).isEqualTo("No se encontró el recurso.");
        assertThat(response.getBody().getUrl()).isEqualTo(request.getRequestURI());
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 400 cuando ocurre IllegalArgumentException")
    void handleIllegalArgumentExceptionRetornaApiErrorConEstado400() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes/buscar");
        IllegalArgumentException exception = new IllegalArgumentException("La solicitud no es válida.");

        ResponseEntity<ApiError> response = handler.handleIllegalArgumentException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.BAD_REQUEST);
        assertThat(response.getBody().getMensaje()).isEqualTo("La solicitud no es válida.");
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 500 cuando ocurre una excepción no controlada")
    void handleGenericExceptionRetornaApiErrorConEstado500() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes");
        Exception exception = new RuntimeException("Error inesperado de prueba.");

        ResponseEntity<ApiError> response = handler.handleGenericException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMensaje()).contains("error inesperado");
    }

    @Test
    @DisplayName("Debe retornar ApiError con estado 400 cuando un parámetro tiene formato inválido")
    void handleMethodArgumentTypeMismatchExceptionRetornaApiErrorConEstado400() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/hoja-vida/estudiantes/filtrar");
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "cuarto", Integer.class, "semestreActual", null, new NumberFormatException());

        ResponseEntity<ApiError> response = handler.handleMethodArgumentTypeMismatchException(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo(ErrorCodes.BAD_REQUEST);
        assertThat(response.getBody().getMensaje()).contains("semestreActual", "formato inválido");
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
