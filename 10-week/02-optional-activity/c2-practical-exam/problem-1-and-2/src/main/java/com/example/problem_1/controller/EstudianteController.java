package com.example.problem_1.controller;

import com.example.problem_1.model.Estudiante;
import com.example.problem_1.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    // GET /estudiantes → 200 OK
    @GetMapping
    public ResponseEntity<List<Estudiante>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // GET /estudiantes/{id} → 200 OK / 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> uno(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    // GET /estudiantes/buscar?nombre=Ana → 200 OK
    @GetMapping("/buscar")
    public ResponseEntity<List<Estudiante>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    // POST /estudiantes → 201 Created / 400 Bad Request
    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante e) {
        Estudiante creado = service.guardar(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /estudiantes/{id} → 200 OK / 404 / 400
    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable Long id,
                                                 @RequestBody Estudiante e) {
        return ResponseEntity.ok(service.actualizar(id, e));
    }

    // DELETE /estudiantes/{id} → 204 No Content / 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}