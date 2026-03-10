package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Cuestionario;
import edu.utvt.evaluacion.data.entities.Pregunta;
import edu.utvt.evaluacion.data.entities.repositories.CuestionarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuestionarioServiceImpl implements CuestionarioService {

    private final CuestionarioRepository repository;

    public CuestionarioServiceImpl(CuestionarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cuestionario crear(String nombre, String descripcion, List<Pregunta> preguntas) {

        Cuestionario c = new Cuestionario();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);

        if (preguntas != null) {
            for (Pregunta p : preguntas) {
                p.setCuestionario(c); // ← ESTA LÍNEA FALTA
            }
        }

        c.setPreguntas(preguntas);

        return repository.save(c);
    }
    @Override
    public Cuestionario obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Cuestionario> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Cuestionario actualizar(Long id, String nombre, String descripcion) {

        Cuestionario c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuestionario no encontrado"));

        c.setNombre(nombre);
        c.setDescripcion(descripcion);

        return repository.save(c);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}