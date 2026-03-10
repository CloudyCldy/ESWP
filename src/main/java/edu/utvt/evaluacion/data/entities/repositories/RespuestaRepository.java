package edu.utvt.evaluacion.data.entities.repositories;

import edu.utvt.evaluacion.data.entities.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    List<Respuesta> findByEvaluacion_IdEvaluacion(Long idEvaluacion);
}
