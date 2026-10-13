package com.example.crud_productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crud_productos.model.Producto;
import com.example.crud_productos.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    // Inyección por constructor
    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto guardar(Producto p) {
        validar(p);
        return repository.save(p);
    }

    public Producto actualizar(Long id, Producto p) {
        validar(p);
        return repository.update(id, p)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public void eliminar(Long id) {
        boolean eliminado = repository.delete(id);
        if (!eliminado) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
    }

    // Validaciones comunes
    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (p.getPrecio() == null || p.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
    }
}