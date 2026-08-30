package com.maestria.gestion.hoja_de_vida.exception;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String detalleValidacion = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BAD_REQUEST,
                detalleValidacion.isBlank()
                        ? "La solicitud contiene errores de validación."
                        : "La solicitud contiene errores de validación: " + detalleValidacion,
                request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex,
            HttpServletRequest request) {
        String detalleValidacion = ex.getConstraintViolations()
                .stream()
                .map(this::formatConstraintViolation)
                .collect(Collectors.joining("; "));

        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BAD_REQUEST,
                detalleValidacion.isBlank()
                        ? "La solicitud contiene errores de validación."
                        : "La solicitud contiene errores de validación: " + detalleValidacion,
                request.getRequestURI());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException ex,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.NOT_FOUND,
                ErrorCodes.RESOURCE_NOT_FOUND,
                "No se encontró la ruta solicitada.",
                request.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        String metodo = ex.getMethod() == null ? "método desconocido" : ex.getMethod();
        return buildError(
                HttpStatus.METHOD_NOT_ALLOWED,
                ErrorCodes.METHOD_NOT_ALLOWED,
                "El método HTTP '" + metodo + "' no está permitido para esta ruta.",
                request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {
        String message = ex.getMessage() == null || ex.getMessage().isBlank()
                ? "No se encontró el recurso solicitado."
                : ex.getMessage();
        return buildError(HttpStatus.NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND, message, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex,
            HttpServletRequest request) {
        String message = ex.getMessage() == null || ex.getMessage().isBlank()
                ? "La solicitud no es válida."
                : ex.getMessage();
        return buildError(HttpStatus.BAD_REQUEST, ErrorCodes.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        String parametro = ex.getParameterName();
        String mensaje = parametro == null || parametro.isBlank()
                ? "Falta un parámetro obligatorio en la solicitud."
                : "El parámetro '" + parametro + "' es obligatorio.";
        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BAD_REQUEST,
                mensaje,
                request.getRequestURI());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestPartException(
            MissingServletRequestPartException ex,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BAD_REQUEST,
                "El archivo '" + ex.getRequestPartName() + "' es obligatorio.",
                request.getRequestURI());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.PAYLOAD_TOO_LARGE,
                ErrorCodes.PAYLOAD_TOO_LARGE,
                "La resolución en PDF no puede superar los 5 MB.",
                request.getRequestURI());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiError> handleMultipartException(
            MultipartException ex,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BAD_REQUEST,
                "No fue posible procesar el archivo adjunto.",
                request.getRequestURI());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ErrorCodes.UNSUPPORTED_MEDIA_TYPE,
                "El tipo de contenido de la solicitud no es compatible.",
                request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String mensaje = ex.getName() == null || ex.getName().isBlank()
                ? "Uno de los parámetros tiene un formato inválido."
                : "El parámetro '" + ex.getName() + "' tiene un formato inválido.";
        return buildError(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.BAD_REQUEST,
                mensaje,
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {} {}", request.getMethod(), request.getRequestURI(), ex);
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCodes.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado.",
                request.getRequestURI());
    }

    private ResponseEntity<ApiError> buildError(HttpStatus estadoHttp, String codigo, String mensaje, String url) {
        ApiError error = new ApiError(
                OffsetDateTime.now(),
                estadoHttp.value(),
                codigo,
                mensaje,
                url);
        return ResponseEntity.status(estadoHttp).body(error);
    }

    private String formatFieldError(FieldError error) {
        if (error.getField() == null || error.getField().isBlank()) {
            return error.getDefaultMessage();
        }
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private String formatConstraintViolation(ConstraintViolation<?> violation) {
        String ruta = violation.getPropertyPath() == null ? "" : violation.getPropertyPath().toString();
        String campo = extractLastNode(ruta);
        if (campo.isBlank()) {
            return violation.getMessage();
        }
        return campo + ": " + violation.getMessage();
    }

    private String extractLastNode(String ruta) {
        if (ruta == null || ruta.isBlank()) {
            return "";
        }
        int indice = ruta.lastIndexOf('.');
        return indice >= 0 ? ruta.substring(indice + 1) : ruta;
    }
}
