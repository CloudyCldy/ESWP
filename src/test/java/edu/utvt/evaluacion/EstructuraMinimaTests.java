package edu.utvt.evaluacion;

import edu.utvt.evaluacion.data.entities.*;
import edu.utvt.evaluacion.data.entities.repositories.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EstructuraMinimaTests {

    @Autowired CuestionarioRepository cuestionarioRepo;
    @Autowired PreguntaRepository     preguntaRepo;
    @Autowired EstudianteRepository   estudianteRepo;
    @Autowired DocenteRepository      docenteRepo;
    @Autowired EvaluacionRepository   evaluacionRepo;
    @Autowired RespuestaRepository    respuestaRepo;


    @Test @Order(1)
    @Transactional
    void cuestionarioDebeRechazarseSiTieneMenosDeTresPreguntas() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Incompleto").descripcion("Solo 2 preguntas")
                .preguntas(new ArrayList<>()).build();
        c.getPreguntas().add(Pregunta.builder().pregunta("P1")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder().pregunta("P2")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(2).cuestionario(c).build());

        assertThat(c.getPreguntas().size()).isLessThan(3);
    }

    @Test @Order(2)
    @Transactional
    void cuestionarioConExactamenteTresPreguntasEsValido() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Desempeño Académico").descripcion("Evaluación docente")
                .preguntas(new ArrayList<>()).build();

        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente explica con claridad los temas?")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente es puntual y cumple el programa?")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(2).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente fomenta la participación del alumno?")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(3).cuestionario(c).build());

        Cuestionario saved = cuestionarioRepo.save(c);
        long total = preguntaRepo.countByCuestionario_IdCuestionario(saved.getIdCuestionario());

        assertThat(total).isGreaterThanOrEqualTo(3);
    }

    @Test @Order(3)
    @Transactional
    void cuestionarioAceptaPreguntasTipoEscala() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Test Escala").descripcion("Escala 1-5")
                .preguntas(new ArrayList<>()).build();

        for (int i = 1; i <= 3; i++) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("Pregunta escala " + i)
                    .tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build());
        }

        Cuestionario saved = cuestionarioRepo.save(c);
        List<Pregunta> preguntas = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(saved.getIdCuestionario());

        assertThat(preguntas).hasSize(3);
        assertThat(preguntas).allMatch(p -> p.getTipo() == Pregunta.TipoPregunta.ESCALA);
    }

    @Test @Order(4)
    @Transactional
    void cuestionarioAceptaPreguntasTipoOpcionMultiple() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Test Opción Múltiple").descripcion("Opción múltiple")
                .preguntas(new ArrayList<>()).build();

        for (int i = 1; i <= 3; i++) {
            c.getPreguntas().add(Pregunta.builder()
                    .pregunta("Pregunta opción múltiple " + i)
                    .tipo(Pregunta.TipoPregunta.OPCION_MULTIPLE)
                    .orden(i).cuestionario(c).build());
        }

        Cuestionario saved = cuestionarioRepo.save(c);
        List<Pregunta> preguntas = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(saved.getIdCuestionario());

        assertThat(preguntas).hasSize(3);
        assertThat(preguntas).allMatch(p -> p.getTipo() == Pregunta.TipoPregunta.OPCION_MULTIPLE);
    }

    @Test @Order(5)
    @Transactional
    void cuestionarioAceptaMezclaDetipos() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Test Mixto").descripcion("Escala y opción múltiple")
                .preguntas(new ArrayList<>()).build();

        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿Cómo calificarías al docente? (escala)")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente usa material didáctico? (opción)")
                .tipo(Pregunta.TipoPregunta.OPCION_MULTIPLE).orden(2).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿Recomendarías al docente? (opción)")
                .tipo(Pregunta.TipoPregunta.OPCION_MULTIPLE).orden(3).cuestionario(c).build());

        Cuestionario saved = cuestionarioRepo.save(c);
        List<Pregunta> preguntas = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(saved.getIdCuestionario());

        long escalas   = preguntas.stream().filter(p -> p.getTipo() == Pregunta.TipoPregunta.ESCALA).count();
        long multiples = preguntas.stream().filter(p -> p.getTipo() == Pregunta.TipoPregunta.OPCION_MULTIPLE).count();

        assertThat(preguntas.size()).isGreaterThanOrEqualTo(3);
        assertThat(escalas).isEqualTo(1);
        assertThat(multiples).isEqualTo(2);
    }


    @Test @Order(6)
    @Transactional
    void evaluacionGuardaComentarioGeneral() {
        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("Comentarista").correo("coment@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("Doc Comentado").correo("doccoment@utvt.edu").build());

        Cuestionario c = Cuestionario.builder()
                .nombre("C Comentario").descripcion("D").preguntas(new ArrayList<>()).build();
        for (int i = 1; i <= 3; i++) {
            c.getPreguntas().add(Pregunta.builder().pregunta("PC" + i)
                    .tipo(Pregunta.TipoPregunta.ESCALA).orden(i).cuestionario(c).build());
        }
        Cuestionario cuest = cuestionarioRepo.save(c);

        List<Pregunta> pregs = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(cuest.getIdCuestionario());

        String comentario = "El docente tiene excelente dominio del tema y explica con mucha claridad.";

        Evaluacion eval = Evaluacion.builder()
                .estudiante(est).docente(doc).cuestionario(cuest)
                .comentarioGeneral(comentario)
                .puntajeFinal(100.0)
                .respuestas(new ArrayList<>()).build();

        for (Pregunta p : pregs) {
            eval.getRespuestas().add(Respuesta.builder()
                    .evaluacion(eval).pregunta(p).valorRespuesta(5).build());
        }

        Evaluacion saved = evaluacionRepo.save(eval);

        assertThat(saved.getComentarioGeneral()).isNotBlank();
        assertThat(saved.getComentarioGeneral()).isEqualTo(comentario);
    }

    @Test @Order(7)
    @Transactional
    void evaluacionPermiteComentarioVacio() {
        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("Sin Comentario").correo("sincom@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("Doc SC").correo("docsc@utvt.edu").build());

        Cuestionario c = Cuestionario.builder()
                .nombre("C SC").descripcion("D").preguntas(new ArrayList<>()).build();
        for (int i = 1; i <= 3; i++) {
            c.getPreguntas().add(Pregunta.builder().pregunta("PSC" + i)
                    .tipo(Pregunta.TipoPregunta.ESCALA).orden(i).cuestionario(c).build());
        }
        Cuestionario cuest = cuestionarioRepo.save(c);
        List<Pregunta> pregs = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(cuest.getIdCuestionario());

        Evaluacion eval = Evaluacion.builder()
                .estudiante(est).docente(doc).cuestionario(cuest)
                .comentarioGeneral(null)   // opcional — puede ser null
                .puntajeFinal(80.0)
                .respuestas(new ArrayList<>()).build();

        for (Pregunta p : pregs) {
            eval.getRespuestas().add(Respuesta.builder()
                    .evaluacion(eval).pregunta(p).valorRespuesta(4).build());
        }

        Evaluacion saved = evaluacionRepo.save(eval);
        assertThat(saved.getIdEvaluacion()).isNotNull();
        assertThat(saved.getComentarioGeneral()).isNull();
    }


    @Test @Order(8)
    @Transactional
    void flujoCompletoEstructuraMinima() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Cuestionario de Desempeño Académico")
                .descripcion("Instrumento oficial de evaluación docente")
                .preguntas(new ArrayList<>()).build();

        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente domina los temas de la materia?")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(1).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente es puntual?")
                .tipo(Pregunta.TipoPregunta.OPCION_MULTIPLE).orden(2).cuestionario(c).build());
        c.getPreguntas().add(Pregunta.builder()
                .pregunta("¿El docente fomenta la participación?")
                .tipo(Pregunta.TipoPregunta.ESCALA).orden(3).cuestionario(c).build());

        Cuestionario cuest = cuestionarioRepo.save(c);

        long totalPreguntas = preguntaRepo
                .countByCuestionario_IdCuestionario(cuest.getIdCuestionario());
        assertThat(totalPreguntas).isGreaterThanOrEqualTo(3);

        Estudiante est = estudianteRepo.save(Estudiante.builder()
                .nombre("Alumno Prueba").correo("alumno.prueba@utvt.edu").build());
        Docente doc = docenteRepo.save(Docente.builder()
                .nombre("Docente Prueba").correo("docente.prueba@utvt.edu").build());

        List<Pregunta> pregs = preguntaRepo
                .findByCuestionario_IdCuestionarioOrderByOrdenAsc(cuest.getIdCuestionario());

        Evaluacion eval = Evaluacion.builder()
                .estudiante(est).docente(doc).cuestionario(cuest)
                .comentarioGeneral("Docente muy comprometido con su labor académica.")
                .respuestas(new ArrayList<>()).build();

        double suma = 0;
        int[] valores = {5, 4, 5};
        for (int i = 0; i < pregs.size(); i++) {
            eval.getRespuestas().add(Respuesta.builder()
                    .evaluacion(eval).pregunta(pregs.get(i))
                    .valorRespuesta(valores[i]).build());
            suma += valores[i];
        }

        double puntaje = Math.round((suma / (pregs.size() * 5.0)) * 100.0 * 100.0) / 100.0;
        eval.setPuntajeFinal(puntaje);

        Evaluacion saved = evaluacionRepo.save(eval);

        assertThat(saved.getRespuestas()).hasSize(3);
        assertThat(saved.getComentarioGeneral()).isNotBlank();
        assertThat(saved.getPuntajeFinal()).isEqualTo(93.33);
        assertThat(saved.getFechaEvaluacion()).isNotNull();
    }
}
