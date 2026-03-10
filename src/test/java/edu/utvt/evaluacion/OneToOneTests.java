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
 * OneToOne implícito: Evaluacion <-> conjunto de Respuestas.
 * Cada Evaluacion tiene una sola instancia por combinación estudiante-docente-cuestionario.
 * También verifica la relación Respuesta -> Pregunta (ManyToOne exclusivo por evaluación).
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OneToOneTests {

    @Autowired EstudianteRepository   estudianteRepo;
    @Autowired DocenteRepository      docenteRepo;
    @Autowired CuestionarioRepository cuestionarioRepo;
    @Autowired PreguntaRepository     preguntaRepo;
    @Autowired EvaluacionRepository   evaluacionRepo;
    @Autowired RespuestaRepository    respuestaRepo;

    @Test @Order(1)
    @Transactional
    void evaluacionConRespuestasUnicasPorPregunta() {
        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("OTO Est").correo("oto.est@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("OTO Doc").correo("oto.doc@utvt.edu").build());

        Cuestionario c = Cuestionario.builder()
                .nombre("OTO Cuestionario").descripcion("D")
                .preguntas(new ArrayList<>()).build();
        for (int i = 1; i <= 3; i++) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("OTO P" + i).tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build());
        }
        Cuestionario cuest = cuestionarioRepo.save(c);

        Evaluacion eval = Evaluacion.builder()
                .estudiante(est).docente(doc).cuestionario(cuest)
                .comentarioGeneral("Excelente desempeño")
                .respuestas(new ArrayList<>()).build();

        List<Pregunta> preguntas = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(cuest.getIdCuestionario());

        int[] valores = {5, 4, 3};
        for (int i = 0; i < preguntas.size(); i++) {
            eval.getRespuestas().add(Respuesta.builder()
                    .evaluacion(eval).pregunta(preguntas.get(i))
                    .valorRespuesta(valores[i]).build());
        }

        double sumaTotal = valores[0] + valores[1] + valores[2]; // 12
        double puntaje = Math.round((sumaTotal / (preguntas.size() * 5.0)) * 100.0 * 100.0) / 100.0;
        eval.setPuntajeFinal(puntaje); // (12/15)*100 = 80.0

        Evaluacion saved = evaluacionRepo.save(eval);

        assertThat(saved.getPuntajeFinal()).isEqualTo(80.0);
        assertThat(saved.getRespuestas()).hasSize(3);

        // Cada respuesta referencia una pregunta diferente
        long preguntasDistintas = saved.getRespuestas().stream()
                .map(r -> r.getPregunta().getIdPregunta()).distinct().count();
        assertThat(preguntasDistintas).isEqualTo(3);
    }

    @Test @Order(2)
    @Transactional
    void respuestaReferenciaCorrectamenteSuPregunta() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Ref Test").descripcion("D")
                .preguntas(new ArrayList<>()).build();
        Pregunta p = Pregunta.builder()
                .pregunta("¿Cómo evalúas al docente?")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build();
        c.getPreguntas().add(p);
        Cuestionario cuest = cuestionarioRepo.save(c);

        Pregunta pregGuardada = cuest.getPreguntas().get(0);

        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("RefEst").correo("refest@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("RefDoc").correo("refdoc@utvt.edu").build());

        Evaluacion eval = Evaluacion.builder()
                .estudiante(est).docente(doc).cuestionario(cuest)
                .puntajeFinal(100.0).comentarioGeneral("Perfecto")
                .respuestas(new ArrayList<>()).build();

        eval.getRespuestas().add(Respuesta.builder()
                .evaluacion(eval).pregunta(pregGuardada).valorRespuesta(5).build());

        Evaluacion saved = evaluacionRepo.save(eval);
        Respuesta respuesta = saved.getRespuestas().get(0);

        assertThat(respuesta.getValorRespuesta()).isEqualTo(5);
        assertThat(respuesta.getPregunta().getPregunta())
                .isEqualTo("¿Cómo evalúas al docente?");
    }

    @Test @Order(3)
    @Transactional
    void puntajeFinalCalculadoCorrectamente() {
        // 3 preguntas escala 1-5, respuestas: 5,5,5 → puntaje = 100.0
        Cuestionario c = Cuestionario.builder()
                .nombre("Puntaje Test").descripcion("D")
                .preguntas(new ArrayList<>()).build();
        for (int i = 1; i <= 3; i++) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("PP" + i).tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build());
        }
        Cuestionario cuest = cuestionarioRepo.save(c);

        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("PuntEst").correo("puntest@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("PuntDoc").correo("puntdoc@utvt.edu").build());

        List<Pregunta> pregs = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(cuest.getIdCuestionario());

        Evaluacion eval = Evaluacion.builder()
                .estudiante(est).docente(doc).cuestionario(cuest)
                .comentarioGeneral("Perfecto").respuestas(new ArrayList<>()).build();

        double suma = 0;
        for (Pregunta preg : pregs) {
            eval.getRespuestas().add(Respuesta.builder()
                    .evaluacion(eval).pregunta(preg).valorRespuesta(5).build());
            suma += 5;
        }
        double puntaje = Math.round((suma / (pregs.size() * 5.0)) * 100.0 * 100.0) / 100.0;
        eval.setPuntajeFinal(puntaje);

        Evaluacion saved = evaluacionRepo.save(eval);
        assertThat(saved.getPuntajeFinal()).isEqualTo(100.0);
    }
}
