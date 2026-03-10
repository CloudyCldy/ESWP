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

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudTests {

    @Autowired EstudianteRepository estudianteRepo;
    @Autowired DocenteRepository    docenteRepo;
    @Autowired CuestionarioRepository cuestionarioRepo;
    @Autowired PreguntaRepository   preguntaRepo;
    @Autowired EvaluacionRepository  evaluacionRepo;
    @Autowired RespuestaRepository   respuestaRepo;

    // ── Estudiante CRUD ──────────────────────────────────────────────────
    @Test @Order(1)
    void crearEstudiante() {
        Estudiante e = Estudiante.builder()
                .nombre("Ana García").correo("ana@utvt.edu").build();
        Estudiante saved = estudianteRepo.save(e);
        assertThat(saved.getIdEstudiante()).isNotNull();
        assertThat(saved.getNombre()).isEqualTo("Ana García");
    }

    @Test @Order(2)
    void leerEstudiante() {
        estudianteRepo.save(Estudiante.builder()
                .nombre("Luis Pérez").correo("luis@utvt.edu").build());
        List<Estudiante> lista = estudianteRepo.findAll();
        assertThat(lista).isNotEmpty();
    }

    @Test @Order(3)
    void actualizarEstudiante() {
        Estudiante e = estudianteRepo.save(Estudiante.builder()
                .nombre("Carlos").correo("carlos@utvt.edu").build());
        e.setNombre("Carlos Actualizado");
        Estudiante actualizado = estudianteRepo.save(e);
        assertThat(actualizado.getNombre()).isEqualTo("Carlos Actualizado");
    }

    @Test @Order(4)
    void eliminarEstudiante() {
        Estudiante e = estudianteRepo.save(Estudiante.builder()
                .nombre("Temporal").correo("temp@utvt.edu").build());
        Long id = e.getIdEstudiante();
        estudianteRepo.deleteById(id);
        assertThat(estudianteRepo.findById(id)).isEmpty();
    }

    // ── Docente CRUD ─────────────────────────────────────────────────────
    @Test @Order(5)
    void crearDocente() {
        Docente d = Docente.builder()
                .nombre("Dr. Martínez").correo("martinez@utvt.edu").build();
        Docente saved = docenteRepo.save(d);
        assertThat(saved.getIdDocente()).isNotNull();
    }

    @Test @Order(6)
    void leerDocente() {
        docenteRepo.save(Docente.builder()
                .nombre("Dra. López").correo("lopez@utvt.edu").build());
        assertThat(docenteRepo.findAll()).isNotEmpty();
    }

    @Test @Order(7)
    void actualizarDocente() {
        Docente d = docenteRepo.save(Docente.builder()
                .nombre("Ing. Torres").correo("torres@utvt.edu").build());
        d.setCorreo("torres.nuevo@utvt.edu");
        Docente act = docenteRepo.save(d);
        assertThat(act.getCorreo()).isEqualTo("torres.nuevo@utvt.edu");
    }

    @Test @Order(8)
    void eliminarDocente() {
        Docente d = docenteRepo.save(Docente.builder()
                .nombre("Temp Docente").correo("dtemp@utvt.edu").build());
        Long id = d.getIdDocente();
        docenteRepo.deleteById(id);
        assertThat(docenteRepo.findById(id)).isEmpty();
    }

    // ── Cuestionario CRUD ────────────────────────────────────────────────
    @Test @Order(9)
    @Transactional
    void crearCuestionario() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Desempeño Académico").descripcion("Evaluación general")
                .preguntas(new ArrayList<>()).build();

        for (int i = 1; i <= 3; i++) {
            Pregunta p = Pregunta.builder()
                    .pregunta("Pregunta " + i)
                    .tipo(Pregunta.TipoPregunta.ESCALA)
                    .orden(i).cuestionario(c).build();
            c.getPreguntas().add(p);
        }

        Cuestionario saved = cuestionarioRepo.save(c);
        assertThat(saved.getIdCuestionario()).isNotNull();
        assertThat(saved.getPreguntas()).hasSize(3);
    }

    @Test @Order(10)
    void leerCuestionarios() {
        assertThat(cuestionarioRepo.findAll()).isNotEmpty();
    }

    @Test @Order(11)
    void actualizarCuestionario() {
        Cuestionario c = Cuestionario.builder()
                .nombre("Original").descripcion("Desc").preguntas(new ArrayList<>()).build();
        Cuestionario saved = cuestionarioRepo.save(c);
        saved.setNombre("Actualizado");
        Cuestionario act = cuestionarioRepo.save(saved);
        assertThat(act.getNombre()).isEqualTo("Actualizado");
    }
}
