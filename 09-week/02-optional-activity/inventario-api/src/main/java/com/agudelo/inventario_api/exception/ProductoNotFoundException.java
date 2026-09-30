package com.agudelo.inventario_api.exception;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(Long id) {
        super("Producto no encontrado con id: " + id);
    }
}