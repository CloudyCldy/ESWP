package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Cuestionario;
import edu.utvt.evaluacion.data.entities.Pregunta;
import java.util.List;

public interface CuestionarioService {
    Cuestionario crear(String nombre, String descripcion, List<Pregunta> preguntas);
    Cuestionario obtenerPorId(Long id);
    List<Cuestionario> obtenerTodos();
    Cuestionario actualizar(Long id, String nombre, String descripcion);
    void eliminar(Long id);
}
