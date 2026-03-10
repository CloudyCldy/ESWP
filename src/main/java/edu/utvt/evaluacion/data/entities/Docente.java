package edu.utvt.evaluacion.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "docentes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Docente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Long idDocente;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 200)
    private String correo;

    @JsonIgnoreProperties("docente")
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evaluacion> evaluaciones;
}
