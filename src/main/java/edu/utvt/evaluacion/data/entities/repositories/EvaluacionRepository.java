package edu.utvt.evaluacion.data.entities.repositories;

import edu.utvt.evaluacion.data.entities.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByEstudiante_IdEstudiante(Long idEstudiante);
    List<Evaluacion> findByDocente_IdDocente(Long idDocente);
    boolean existsByEstudiante_IdEstudianteAndDocente_IdDocenteAndCuestionario_IdCuestionario(
            Long idEstudiante, Long idDocente, Long idCuestionario);

    @Query("SELECT AVG(e.puntajeFinal) FROM Evaluacion e WHERE e.docente.idDocente = :idDocente")
    Double calcularPromedioPuntajeByDocente(Long idDocente);
}
