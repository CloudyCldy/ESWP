package edu.utvt.evaluacion.common;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " con id " + id + " no fue encontrado");
    }
}
