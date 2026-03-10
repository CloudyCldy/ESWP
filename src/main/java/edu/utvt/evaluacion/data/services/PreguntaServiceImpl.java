package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Pregunta;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PreguntaServiceImpl implements PreguntaService {

    @Override
    public Pregunta agregar(Long idCuestionario, String textoPregunta, Pregunta.TipoPregunta tipo, Integer orden) {
        return null;
    }

    @Override
    public Pregunta obtenerPorId(Long id) {
        return null;
    }

    @Override
    public List<Pregunta> obtenerPorCuestionario(Long idCuestionario) {
        return null;
    }

    @Override
    public Pregunta actualizar(Long id, String textoPregunta, Pregunta.TipoPregunta tipo, Integer orden) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }
}