package edu.utvt.evaluacion.data.entities.repositories;

import edu.utvt.evaluacion.data.entities.Cuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Long> {

    @Query("SELECT c FROM Cuestionario c LEFT JOIN FETCH c.preguntas WHERE c.idCuestionario = :id")
    Optional<Cuestionario> findByIdWithPreguntas(Long id);
}
