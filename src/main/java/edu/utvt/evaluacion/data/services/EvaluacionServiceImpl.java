package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Evaluacion;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class EvaluacionServiceImpl implements EvaluacionService {

    @Override
    public Evaluacion crear(Long idEstudiante, Long idDocente, Long idCuestionario,
                            String comentarioGeneral, Map<Long, Integer> respuestas) {
        return null;
    }

    @Override
    public Evaluacion obtenerPorId(Long id) {
        return null;
    }

    @Override
    public List<Evaluacion> obtenerTodas() {
        return null;
    }

    @Override
    public List<Evaluacion> obtenerPorEstudiante(Long idEstudiante) {
        return null;
    }

    @Override
    public List<Evaluacion> obtenerPorDocente(Long idDocente) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }
}