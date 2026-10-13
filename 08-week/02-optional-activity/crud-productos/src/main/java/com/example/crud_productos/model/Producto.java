package com.example.crud_productos.model;

public class Producto {

    private Long id;
    private String nombre;
    private Double precio;

    // Constructor vacío (obligatorio para Jackson)
    public Producto() {
    }

    // Constructor con parámetros (cómodo para crear objetos)
    public Producto(Long id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}