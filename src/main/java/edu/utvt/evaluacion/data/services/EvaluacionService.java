package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Evaluacion;
import java.util.List;
import java.util.Map;

public interface EvaluacionService {
    // respuestas = Map<idPregunta, valorRespuesta>
    Evaluacion crear(Long idEstudiante, Long idDocente, Long idCuestionario,
                     String comentarioGeneral, Map<Long, Integer> respuestas);
    Evaluacion obtenerPorId(Long id);
    List<Evaluacion> obtenerTodas();
    List<Evaluacion> obtenerPorEstudiante(Long idEstudiante);
    List<Evaluacion> obtenerPorDocente(Long idDocente);
    void eliminar(Long id);
}
