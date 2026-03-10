package edu.utvt.evaluacion.controllers;

import edu.utvt.evaluacion.common.ApiResponse;
import edu.utvt.evaluacion.data.entities.Evaluacion;
import edu.utvt.evaluacion.data.services.EvaluacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService service;

    record RespuestaInline(
            @NotNull Long idPregunta,
            @NotNull @Min(1) @Max(5) Integer valorRespuesta
    ) {}

    record EvaluacionRequest(
            @NotNull Long idEstudiante,
            @NotNull Long idDocente,
            @NotNull Long idCuestionario,
            @Size(max = 1000) String comentarioGeneral,
            @NotNull @Size(min = 1) List<RespuestaInline> respuestas
    ) {}

    @PostMapping
    public ResponseEntity<ApiResponse<Evaluacion>> crear(
            @Valid @RequestBody EvaluacionRequest req) {
        Map<Long, Integer> respuestasMap = req.respuestas().stream()
                .collect(Collectors.toMap(RespuestaInline::idPregunta, RespuestaInline::valorRespuesta));
        Evaluacion evaluacion = service.crear(
                req.idEstudiante(), req.idDocente(), req.idCuestionario(),
                req.comentarioGeneral(), respuestasMap);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Evaluación registrada", evaluacion));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Evaluacion>>> obtenerTodas() {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Evaluacion>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorId(id)));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<ApiResponse<List<Evaluacion>>> porEstudiante(
            @PathVariable Long idEstudiante) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorEstudiante(idEstudiante)));
    }

    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<ApiResponse<List<Evaluacion>>> porDocente(
            @PathVariable Long idDocente) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorDocente(idDocente)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Evaluación eliminada", null));
    }
}
