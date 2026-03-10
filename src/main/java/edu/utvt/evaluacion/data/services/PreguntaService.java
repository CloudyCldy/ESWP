package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Pregunta;
import java.util.List;

public interface PreguntaService {
    Pregunta agregar(Long idCuestionario, String textoPregunta, Pregunta.TipoPregunta tipo, Integer orden);
    Pregunta obtenerPorId(Long id);
    List<Pregunta> obtenerPorCuestionario(Long idCuestionario);
    Pregunta actualizar(Long id, String textoPregunta, Pregunta.TipoPregunta tipo, Integer orden);
    void eliminar(Long id);
}
