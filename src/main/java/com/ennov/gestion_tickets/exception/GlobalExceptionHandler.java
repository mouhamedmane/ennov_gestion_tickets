package com.ennov.gestion_tickets.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import com.ennov.gestion_tickets.responseApi.ResponseApi;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseApi<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(error -> (error instanceof FieldError ? ((FieldError) error).getField() + " : " : "") + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .badRequest()
                .body(ResponseApi.fail(errorMessages));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseApi<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(ResponseApi.fail(List.of("La requête n'est pas lisible ou est malformée")));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseApi<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED)
                .body(ResponseApi.fail(List.of("La méthode HTTP n'est pas prise en charge pour ce endpoint")));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseApi<Object>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseApi.fail(List.of("Une erreur interne est survenue")));
    }
}
