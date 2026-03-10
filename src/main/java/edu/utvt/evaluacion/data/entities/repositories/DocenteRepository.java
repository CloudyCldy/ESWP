package edu.utvt.evaluacion.data.entities.repositories;

import edu.utvt.evaluacion.data.entities.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
