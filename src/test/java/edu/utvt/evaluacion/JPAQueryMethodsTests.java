package edu.utvt.evaluacion;

import edu.utvt.evaluacion.data.entities.*;
import edu.utvt.evaluacion.data.entities.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JPAQueryMethodsTests {

    @Autowired EstudianteRepository  estudianteRepo;
    @Autowired DocenteRepository     docenteRepo;
    @Autowired CuestionarioRepository cuestionarioRepo;
    @Autowired PreguntaRepository    preguntaRepo;
    @Autowired EvaluacionRepository  evaluacionRepo;

    @Test @Order(1)
    void findEstudianteByCorreo() {
        estudianteRepo.save(Estudiante.builder()
                .nombre("Query Test").correo("query@utvt.edu").build());
        Optional<Estudiante> result = estudianteRepo.findByCorreo("query@utvt.edu");
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Query Test");
    }

    @Test @Order(2)
    void existsEstudianteByCorreo() {
        estudianteRepo.save(Estudiante.builder()
                .nombre("Exists Test").correo("exists@utvt.edu").build());
        assertThat(estudianteRepo.existsByCorreo("exists@utvt.edu")).isTrue();
        assertThat(estudianteRepo.existsByCorreo("noexiste@utvt.edu")).isFalse();
    }

    @Test @Order(3)
    void findDocenteByCorreo() {
        docenteRepo.save(Docente.builder()
                .nombre("Docente Query").correo("dquery@utvt.edu").build());
        Optional<Docente> result = docenteRepo.findByCorreo("dquery@utvt.edu");
        assertThat(result).isPresent();
    }

    @Test @Order(4)
    @Transactional
    void findPreguntasByCuestionarioOrdenadas() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Cuestionario Query").descripcion("Test")
                .preguntas(new ArrayList<>()).build();
        for (int i = 3; i >= 1; i--) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("P" + i).tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build());
        }
        Cuestionario saved = cuestionarioRepo.save(c);

        List<Pregunta> preguntas = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(saved.getIdCuestionario());
        assertThat(preguntas).hasSize(3);
        assertThat(preguntas.get(0).getOrden()).isEqualTo(1);
        assertThat(preguntas.get(2).getOrden()).isEqualTo(3);
    }

    @Test @Order(5)
    @Transactional
    void countPreguntasByCuestionario() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Count Test").descripcion("Desc")
                .preguntas(new ArrayList<>()).build();
        for (int i = 1; i <= 4; i++) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("P" + i).tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build());
        }
        Cuestionario saved = cuestionarioRepo.save(c);
        long count = preguntaRepo.countByCuestionario_IdCuestionario(saved.getIdCuestionario());
        assertThat(count).isEqualTo(4);
    }

    @Test @Order(6)
    @Transactional
    void calcularPromedioPuntajeDocente() {
        Docente docente = docenteRepo.save(Docente.builder()
                .nombre("Promedio Docente").correo("promedio@utvt.edu").build());

        Estudiante est1 = estudianteRepo.save(Estudiante.builder()
                .nombre("Est1").correo("est1prom@utvt.edu").build());
        Estudiante est2 = estudianteRepo.save(Estudiante.builder()
                .nombre("Est2").correo("est2prom@utvt.edu").build());

        Cuestionario c = Cuestionario.builder()
                .nombre("C Promedio").descripcion("D").preguntas(new ArrayList<>()).build();
        c.getPreguntas().add(Pregunta.builder().pregunta("P1")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build());
        Cuestionario cuest = cuestionarioRepo.save(c);

        Evaluacion e1 = Evaluacion.builder().estudiante(est1).docente(docente)
                .cuestionario(cuest).puntajeFinal(80.0)
                .comentarioGeneral("Bien").respuestas(new ArrayList<>()).build();
        Evaluacion e2 = Evaluacion.builder().estudiante(est2).docente(docente)
                .cuestionario(cuest).puntajeFinal(60.0)
                .comentarioGeneral("Regular").respuestas(new ArrayList<>()).build();
        evaluacionRepo.save(e1);
        evaluacionRepo.save(e2);

        Double promedio = evaluacionRepo.calcularPromedioPuntajeByDocente(docente.getIdDocente());
        assertThat(promedio).isEqualTo(70.0);
    }

    @Test @Order(7)
    @Transactional
    void findEvaluacionesByDocente() {
        Docente d = docenteRepo.save(Docente.builder()
                .nombre("Docente Filter").correo("filter@utvt.edu").build());
        Estudiante e = estudianteRepo.save(Estudiante.builder()
                .nombre("EstFilter").correo("estfilter@utvt.edu").build());
        Cuestionario c = Cuestionario.builder()
                .nombre("C Filter").descripcion("D").preguntas(new ArrayList<>()).build();
        c.getPreguntas().add(Pregunta.builder().pregunta("PF")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build());
        Cuestionario cuest = cuestionarioRepo.save(c);

        evaluacionRepo.save(Evaluacion.builder().estudiante(e).docente(d)
                .cuestionario(cuest).puntajeFinal(75.0)
                .comentarioGeneral("OK").respuestas(new ArrayList<>()).build());

        List<Evaluacion> resultados = evaluacionRepo.findByDocente_IdDocente(d.getIdDocente());
        assertThat(resultados).hasSize(1);
    }
}
