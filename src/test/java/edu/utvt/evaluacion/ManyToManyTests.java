package edu.utvt.evaluacion;

import edu.utvt.evaluacion.data.entities.*;
import edu.utvt.evaluacion.data.entities.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ManyToMany implícito: Estudiante <-> Docente a través de Evaluacion.
 * Un estudiante evalúa a muchos docentes y un docente es evaluado por muchos estudiantes.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManyToManyTests {

    @Autowired EstudianteRepository  estudianteRepo;
    @Autowired DocenteRepository     docenteRepo;
    @Autowired CuestionarioRepository cuestionarioRepo;
    @Autowired EvaluacionRepository  evaluacionRepo;

    @Test @Order(1)
    @Transactional
    void unEstudianteEvaluaVariosDocentes() {
        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("Evaluador Multi").correo("evalmulti@utvt.edu").build());

        for (int i = 1; i <= 3; i++) {
            Docente doc = docenteRepo.save(Docente.builder()
                    .nombre("Doc " + i).correo("doc" + i + "m@utvt.edu").build());
            Cuestionario c = cuestionarioRepo.save(Cuestionario.builder()
                    .nombre("CM" + i).descripcion("D").preguntas(new ArrayList<>()).build());
            evaluacionRepo.save(Evaluacion.builder()
                    .estudiante(est).docente(doc).cuestionario(c)
                    .puntajeFinal(85.0).comentarioGeneral("OK")
                    .respuestas(new ArrayList<>()).build());
        }

        List<Evaluacion> evals = evaluacionRepo.findByEstudiante_IdEstudiante(est.getIdEstudiante());
        long docentesDistintos = evals.stream()
                .map(e -> e.getDocente().getIdDocente()).distinct().count();
        assertThat(docentesDistintos).isEqualTo(3);
    }

    @Test @Order(2)
    @Transactional
    void unDocenteEsEvaluadoPorVariosEstudiantes() {
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("Evaluado Multi").correo("evaldoms@utvt.edu").build());
        Cuestionario c = cuestionarioRepo.save(Cuestionario.builder()
                .nombre("CMD").descripcion("D").preguntas(new ArrayList<>()).build());

        for (int i = 1; i <= 4; i++) {
            Estudiante est = estudianteRepo.save(Estudiante.builder()
                    .nombre("Est " + i).correo("estm" + i + "@utvt.edu").build());
            evaluacionRepo.save(Evaluacion.builder()
                    .estudiante(est).docente(doc).cuestionario(c)
                    .puntajeFinal(75.0).comentarioGeneral("Bien")
                    .respuestas(new ArrayList<>()).build());
        }

        assertThat(evaluacionRepo.findByDocente_IdDocente(doc.getIdDocente())).hasSize(4);
    }

    @Test @Order(3)
    @Transactional
    void noPermiteDuplicadoEstudianteDocenteCuestionario() {
        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("NoDup").correo("nodup@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("NoDup Doc").correo("nodupd@utvt.edu").build());
        Cuestionario c = cuestionarioRepo.save(Cuestionario.builder()
                .nombre("NoDupC").descripcion("D").preguntas(new ArrayList<>()).build());

        boolean existeAntes = evaluacionRepo
                .existsByEstudiante_IdEstudianteAndDocente_IdDocenteAndCuestionario_IdCuestionario(
                        est.getIdEstudiante(), doc.getIdDocente(), c.getIdCuestionario());
        assertThat(existeAntes).isFalse();

        evaluacionRepo.save(Evaluacion.builder().estudiante(est).docente(doc)
                .cuestionario(c).puntajeFinal(80.0).comentarioGeneral("OK")
                .respuestas(new ArrayList<>()).build());

        boolean existeDespues = evaluacionRepo
                .existsByEstudiante_IdEstudianteAndDocente_IdDocenteAndCuestionario_IdCuestionario(
                        est.getIdEstudiante(), doc.getIdDocente(), c.getIdCuestionario());
        assertThat(existeDespues).isTrue();
    }
}
