package edu.utvt.evaluacion.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "preguntas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pregunta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long idPregunta;

    @JsonIgnoreProperties({"preguntas", "evaluaciones"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuestionario", nullable = false)
    private Cuestionario cuestionario;

    @Column(nullable = false, length = 500)
    private String pregunta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPregunta tipo;

    @Column(name = "orden")
    private Integer orden;

    @JsonIgnoreProperties("pregunta")
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas;

    public enum TipoPregunta { ESCALA, OPCION_MULTIPLE }
}
