package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Docente;
import edu.utvt.evaluacion.data.entities.repositories.DocenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository repository;

    public DocenteServiceImpl(DocenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Docente crear(String nombre, String correo) {

        Docente docente = new Docente();
        docente.setNombre(nombre);
        docente.setCorreo(correo);

        return repository.save(docente);
    }

    @Override
    public Docente obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Docente> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Docente actualizar(Long id, String nombre, String correo) {

        Docente docente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        docente.setNombre(nombre);
        docente.setCorreo(correo);

        return repository.save(docente);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}