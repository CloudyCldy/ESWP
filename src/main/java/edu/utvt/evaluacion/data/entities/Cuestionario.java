package edu.utvt.evaluacion.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cuestionarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cuestionario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuestionario")
    private Long idCuestionario;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @JsonIgnoreProperties("cuestionario")
    @OneToMany(mappedBy = "cuestionario", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Pregunta> preguntas;

    @JsonIgnoreProperties("cuestionario")
    @OneToMany(mappedBy = "cuestionario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evaluacion> evaluaciones;
}
