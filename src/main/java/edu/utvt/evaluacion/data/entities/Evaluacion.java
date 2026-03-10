package edu.utvt.evaluacion.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evaluacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Long idEvaluacion;

    @JsonIgnoreProperties("evaluaciones")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @JsonIgnoreProperties("evaluaciones")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente docente;

    @JsonIgnoreProperties({"preguntas", "evaluaciones"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuestionario", nullable = false)
    private Cuestionario cuestionario;

    @Column(name = "puntaje_final")
    private Double puntajeFinal;

    @Column(name = "comentario_general", length = 1000)
    private String comentarioGeneral;

    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;

    @JsonIgnoreProperties("evaluacion")
    @OneToMany(mappedBy = "evaluacion", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Respuesta> respuestas;

    @PrePersist
    public void prePersist() { this.fechaEvaluacion = LocalDateTime.now(); }
}
