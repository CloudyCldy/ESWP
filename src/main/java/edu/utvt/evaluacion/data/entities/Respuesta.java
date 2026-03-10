package edu.utvt.evaluacion.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "respuestas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Respuesta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Long idRespuesta;

    @JsonIgnoreProperties("respuestas")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evaluacion", nullable = false)
    private Evaluacion evaluacion;

    @JsonIgnoreProperties({"respuestas", "cuestionario"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;

    @Column(name = "valor_respuesta", nullable = false)
    private Integer valorRespuesta;
}
