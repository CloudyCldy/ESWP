package edu.utvt.evaluacion.controllers;

import edu.utvt.evaluacion.common.ApiResponse;
import edu.utvt.evaluacion.data.entities.Pregunta;
import edu.utvt.evaluacion.data.services.PreguntaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/preguntas")
@RequiredArgsConstructor
public class PreguntaController {

    private final PreguntaService service;

    record PreguntaRequest(
            @NotNull Long idCuestionario,
            @NotBlank String pregunta,
            @NotNull Pregunta.TipoPregunta tipo,
            Integer orden
    ) {}

    record PreguntaUpdateRequest(
            @NotBlank String pregunta,
            @NotNull Pregunta.TipoPregunta tipo,
            Integer orden
    ) {}

    @PostMapping
    public ResponseEntity<ApiResponse<Pregunta>> agregar(
            @Valid @RequestBody PreguntaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pregunta agregada",
                        service.agregar(req.idCuestionario(), req.pregunta(), req.tipo(), req.orden())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pregunta>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorId(id)));
    }

    @GetMapping("/cuestionario/{idCuestionario}")
    public ResponseEntity<ApiResponse<List<Pregunta>>> obtenerPorCuestionario(
            @PathVariable Long idCuestionario) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorCuestionario(idCuestionario)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Pregunta>> actualizar(
            @PathVariable Long id, @Valid @RequestBody PreguntaUpdateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Pregunta actualizada",
                service.actualizar(id, req.pregunta(), req.tipo(), req.orden())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Pregunta eliminada", null));
    }
}
