package com.example.crud_productos.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.crud_productos.model.Producto;

@Repository
public class ProductoRepository {

    private final List<Producto> productos = new ArrayList<>();
    private Long contadorId = 1L;

    // Listar todos
    public List<Producto> findAll() {
        return productos;
    }

    // Buscar por id
    public Optional<Producto> findById(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // Guardar uno nuevo (asigna id automáticamente)
    public Producto save(Producto p) {
        p.setId(contadorId++);
        productos.add(p);
        return p;
    }

    // Actualizar uno existente
    public Optional<Producto> update(Long id, Producto datos) {
        return findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setPrecio(datos.getPrecio());
            return existente;
        });
    }

    // Eliminar por id
    public boolean delete(Long id) {
        return productos.removeIf(p -> p.getId().equals(id));
    }
}