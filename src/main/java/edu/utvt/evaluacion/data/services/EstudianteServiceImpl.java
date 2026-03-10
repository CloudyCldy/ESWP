package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Estudiante;
import edu.utvt.evaluacion.data.entities.repositories.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteServiceImpl(EstudianteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Estudiante crear(String nombre, String correo) {

        if(repository.existsByCorreo(correo)){
            throw new RuntimeException("El correo ya existe");
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);

        return repository.save(estudiante);
    }

    @Override
    public Estudiante obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Estudiante actualizar(Long id, String nombre, String correo) {

        Estudiante estudiante = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);

        return repository.save(estudiante);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}