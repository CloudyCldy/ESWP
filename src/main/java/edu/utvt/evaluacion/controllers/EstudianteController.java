package edu.utvt.evaluacion.controllers;

import edu.utvt.evaluacion.common.ApiResponse;
import edu.utvt.evaluacion.data.entities.Estudiante;
import edu.utvt.evaluacion.data.services.EstudianteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService service;

    record EstudianteRequest(
            @NotBlank String nombre,
            @NotBlank @Email String correo
    ) {}

    @PostMapping
    public ResponseEntity<ApiResponse<Estudiante>> crear(
            @Valid @RequestBody EstudianteRequest req) {
        Estudiante creado = service.crear(req.nombre(), req.correo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Estudiante creado", creado));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Estudiante>>> obtenerTodos() {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Estudiante>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Estudiante>> actualizar(
            @PathVariable Long id, @Valid @RequestBody EstudianteRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Estudiante actualizado",
                service.actualizar(id, req.nombre(), req.correo())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Estudiante eliminado", null));
    }
}
