package com.example.crud_productos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crud_productos.model.Producto;
import com.example.crud_productos.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    // Inyección por constructor
    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // GET /productos
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // GET /productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> uno(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    // POST /productos
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto p) {
        Producto creado = service.guardar(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                               @RequestBody Producto p) {
        Producto actualizado = service.actualizar(id, p);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}