package br.edu.ufape.personal_trainer.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildResponse(400, "Erro de validação", "Dados inválidos", request.getRequestURI(), errors);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessValidation(
            BusinessValidationException ex, HttpServletRequest request) {
        return buildResponse(400, "Validação de negócio", "Dados inválidos", request.getRequestURI(), ex.getErrors());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleBusinessRule(Exception ex, HttpServletRequest request) {
        Map<String, String> detail = new HashMap<>();
        detail.put("erro", ex.getMessage());
        return buildResponse(400, "Regra de negócio", ex.getMessage(), request.getRequestURI(), detail);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        // Usa a mensagem real passada pelo AccessDeniedException
        String msg = ex.getMessage() != null ? ex.getMessage() : "Você não tem permissão para acessar este recurso";
        return buildResponse(403, "Acesso negado", msg, request.getRequestURI(), null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationError(AuthenticationException ex, HttpServletRequest request) {
        return buildResponse(401, "Não autenticado", "Credenciais inválidas ou ausentes", request.getRequestURI(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(404, "Não encontrado", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalError(Exception ex, HttpServletRequest request) {
        return buildResponse(500, "Erro interno", "Ocorreu um erro inesperado", request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            int status, String error, String message, String path, Map<String, String> detail) {

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now());
        response.setStatus(status);
        response.setError(error);
        response.setMessage(message);
        response.setPath(path);
        response.setDetail(detail);

        return ResponseEntity.status(status).body(response);
    }
}
