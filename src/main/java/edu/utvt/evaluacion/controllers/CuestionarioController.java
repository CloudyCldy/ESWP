package edu.utvt.evaluacion.controllers;

import edu.utvt.evaluacion.common.ApiResponse;
import edu.utvt.evaluacion.data.entities.Cuestionario;
import edu.utvt.evaluacion.data.entities.Pregunta;
import edu.utvt.evaluacion.data.services.CuestionarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cuestionarios")
@RequiredArgsConstructor
public class CuestionarioController {

    private final CuestionarioService service;

    record PreguntaInline(
            @NotBlank String pregunta,
            @NotNull Pregunta.TipoPregunta tipo,
            Integer orden
    ) {}

    record CuestionarioRequest(
            @NotBlank String nombre,
            String descripcion,
            @NotNull @Size(min = 3, message = "Mínimo 3 preguntas") List<PreguntaInline> preguntas
    ) {}

    record CuestionarioUpdateRequest(
            @NotBlank String nombre,
            String descripcion
    ) {}

    @PostMapping
    public ResponseEntity<ApiResponse<Cuestionario>> crear(
            @Valid @RequestBody CuestionarioRequest req) {
        List<Pregunta> preguntas = req.preguntas().stream()
                .map(p -> Pregunta.builder()
                        .pregunta(p.pregunta()).tipo(p.tipo()).orden(p.orden()).build())
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cuestionario creado",
                        service.crear(req.nombre(), req.descripcion(), preguntas)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Cuestionario>>> obtenerTodos() {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cuestionario>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cuestionario>> actualizar(
            @PathVariable Long id, @Valid @RequestBody CuestionarioUpdateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Cuestionario actualizado",
                service.actualizar(id, req.nombre(), req.descripcion())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Cuestionario eliminado", null));
    }
}
