package actividad_jpa.service;

import actividad_jpa.entity.Producto;
import actividad_jpa.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // CREATE
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // READ - listar todos
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    // READ - buscar por ID
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    // READ - buscar por nombre
    public List<Producto> buscarPorNombre(String texto) {
        return productoRepository.findByNombreContaining(texto);
    }

    // UPDATE
    public Producto actualizar(Producto producto) {
        return productoRepository.save(producto);
    }

    // DELETE
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}