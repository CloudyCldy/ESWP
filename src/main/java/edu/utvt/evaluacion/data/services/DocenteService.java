package edu.utvt.evaluacion.data.services;

import edu.utvt.evaluacion.data.entities.Docente;
import java.util.List;

public interface DocenteService {
    Docente crear(String nombre, String correo);
    Docente obtenerPorId(Long id);
    List<Docente> obtenerTodos();
    Docente actualizar(Long id, String nombre, String correo);
    void eliminar(Long id);
}
