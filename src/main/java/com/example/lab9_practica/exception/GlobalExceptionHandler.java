package com.example.lab9_practica.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// PREGUNTA 5: Manejo centralizado de excepciones y validaciones del servicio
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Captura todos los escenarios de negocio y validacion (Equipo no encontrado, duplicados, correo/telefono/jugadores invalidos)
    @ExceptionHandler(EquipoException.class)
    public ResponseEntity<Map<String, Object>> manejarEquipoException(EquipoException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", "error");
        response.put("status", ex.getStatus().value());
        response.put("mensaje", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    // 2. Captura cuando no se envia cuerpo JSON o el JSON esta mal formado (como en Clase 9.2 Slide 38)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarJsonInvalido(HttpServletRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("result", "error");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("mensaje", "El cuerpo de la petición JSON es inválido o está vacío en el método " + request.getMethod());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 3. Captura errores de @Valid en caso se utilicen anotaciones de Jakarta Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));

        response.put("result", "error");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("mensaje", "Error de validación en los datos enviados");
        response.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
