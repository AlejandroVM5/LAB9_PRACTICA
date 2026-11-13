package com.example.lab9_practica.exception;

import org.springframework.http.HttpStatus;

// Excepcion personalizada que almacena el mensaje claro para el usuario y el codigo HTTP apropiado
public class EquipoException extends RuntimeException {

    private final HttpStatus status;

    public EquipoException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
