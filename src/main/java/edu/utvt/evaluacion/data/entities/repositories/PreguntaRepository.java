package edu.utvt.evaluacion.data.entities.repositories;

import edu.utvt.evaluacion.data.entities.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByCuestionario_IdCuestionarioOrderByOrdenAsc(Long idCuestionario);
    long countByCuestionario_IdCuestionario(Long idCuestionario);
}
