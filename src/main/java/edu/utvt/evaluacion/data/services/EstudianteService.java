package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Estudiante;
import java.util.List;

public interface EstudianteService {
    Estudiante crear(String nombre, String correo);
    Estudiante obtenerPorId(Long id);
    List<Estudiante> obtenerTodos();
    Estudiante actualizar(Long id, String nombre, String correo);
    void eliminar(Long id);
}
