package actividad_jpa.controller;

import actividad_jpa.entity.Producto;
import actividad_jpa.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // CREATE
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // READ - listar todos
    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    // READ - buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ - buscar por nombre
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        return productoService.buscarPorId(id)
                .map(productoExistente -> {
                    producto.setId(id);
                    return ResponseEntity.ok(
                            productoService.actualizar(producto)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
