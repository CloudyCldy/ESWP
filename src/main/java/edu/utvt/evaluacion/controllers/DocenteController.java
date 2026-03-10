package edu.utvt.evaluacion.controllers;

import edu.utvt.evaluacion.common.ApiResponse;
import edu.utvt.evaluacion.data.entities.Docente;
import edu.utvt.evaluacion.data.services.DocenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService service;

    record DocenteRequest(
            @NotBlank String nombre,
            @NotBlank @Email String correo
    ) {}

    @PostMapping
    public ResponseEntity<ApiResponse<Docente>> crear(
            @Valid @RequestBody DocenteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Docente creado", service.crear(req.nombre(), req.correo())));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Docente>>> obtenerTodos() {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Docente>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Docente>> actualizar(
            @PathVariable Long id, @Valid @RequestBody DocenteRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Docente actualizado",
                service.actualizar(id, req.nombre(), req.correo())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Docente eliminado", null));
    }
}
