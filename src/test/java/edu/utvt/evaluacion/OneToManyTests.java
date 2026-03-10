package edu.utvt.evaluacion;

import edu.utvt.evaluacion.data.entities.*;
import edu.utvt.evaluacion.data.entities.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OneToManyTests {

    @Autowired CuestionarioRepository cuestionarioRepo;
    @Autowired PreguntaRepository     preguntaRepo;
    @Autowired EstudianteRepository   estudianteRepo;
    @Autowired DocenteRepository      docenteRepo;
    @Autowired EvaluacionRepository   evaluacionRepo;

    @Test @Order(1)
    @Transactional
    void cuestionarioTieneVariasPreguntas() {
        Cuestionario c = Cuestionario.builder()
                .nombre("OneToMany Test").descripcion("Desc")
                .preguntas(new ArrayList<>()).build();
        for (int i = 1; i <= 5; i++) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("Pregunta " + i)
                    .tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build());
        }
        Cuestionario saved = cuestionarioRepo.save(c);
        assertThat(saved.getPreguntas()).hasSize(5);
    }

    @Test @Order(2)
    @Transactional
    void eliminarCuestionarioEliminaPreguntas() {
        Cuestionario c = Cuestionario.builder()
                .nombre("A Eliminar").descripcion("D")
                .preguntas(new ArrayList<>()).build();
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("P1").tipo(Pregunta.TipoPregunta.ESCALA)
                .orden(1).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("P2").tipo(Pregunta.TipoPregunta.ESCALA)
                .orden(2).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("P3").tipo(Pregunta.TipoPregunta.ESCALA)
                .orden(3).cuestionario(c).build());
        Cuestionario saved = cuestionarioRepo.save(c);
        Long idCuest = saved.getIdCuestionario();

        cuestionarioRepo.delete(saved);

        assertThat(cuestionarioRepo.findById(idCuest)).isEmpty();
        assertThat(preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(idCuest)).isEmpty();
    }

    @Test @Order(3)
    @Transactional
    void estudianteTieneVariasEvaluaciones() {
        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("MultiEval").correo("multieval@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("DocMulti").correo("docmulti@utvt.edu").build());

        Cuestionario c1 = cuestionarioRepo.save(Cuestionario.builder()
                .nombre("C1").descripcion("D").preguntas(new ArrayList<>()).build());
        Cuestionario c2 = cuestionarioRepo.save(Cuestionario.builder()
                .nombre("C2").descripcion("D").preguntas(new ArrayList<>()).build());

        evaluacionRepo.save(Evaluacion.builder().estudiante(est).docente(doc)
                .cuestionario(c1).puntajeFinal(90.0)
                .comentarioGeneral("Excelente").respuestas(new ArrayList<>()).build());
        evaluacionRepo.save(Evaluacion.builder().estudiante(est).docente(doc)
                .cuestionario(c2).puntajeFinal(70.0)
                .comentarioGeneral("Bueno").respuestas(new ArrayList<>()).build());

        assertThat(evaluacionRepo.findByEstudiante_IdEstudiante(est.getIdEstudiante()))
                .hasSize(2);
    }

    @Test @Order(4)
    @Transactional
    void docenteAcumulaEvaluaciones() {
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("DocAcum").correo("docacum@utvt.edu").build());

        for (int i = 1; i <= 3; i++) {
            Estudiante est = estudianteRepo.save(Estudiante.builder()
                    .nombre("E" + i).correo("e" + i + "acum@utvt.edu").build());
            Cuestionario c = cuestionarioRepo.save(Cuestionario.builder()
                    .nombre("CA" + i).descripcion("D").preguntas(new ArrayList<>()).build());
            evaluacionRepo.save(Evaluacion.builder().estudiante(est).docente(doc)
                    .cuestionario(c).puntajeFinal(80.0)
                    .comentarioGeneral("C" + i).respuestas(new ArrayList<>()).build());
        }

        assertThat(evaluacionRepo.findByDocente_IdDocente(doc.getIdDocente())).hasSize(3);
    }
}
