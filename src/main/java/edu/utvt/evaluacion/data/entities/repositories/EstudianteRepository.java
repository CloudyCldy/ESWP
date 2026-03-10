package edu.utvt.evaluacion.data.entities.repositories;

import edu.utvt.evaluacion.data.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
