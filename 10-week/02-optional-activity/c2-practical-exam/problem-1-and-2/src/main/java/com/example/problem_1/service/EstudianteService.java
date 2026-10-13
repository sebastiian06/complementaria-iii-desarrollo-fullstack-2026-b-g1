package com.example.problem_1;

import com.example.problem_1.model.Estudiante;
import com.example.problem_1.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public List<Estudiante> listar() {
        return repository.findAll();
    }

    public Estudiante buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + id));
    }

    public Estudiante guardar(Estudiante e) {
        validar(e);
        return repository.save(e);
    }

    public Estudiante actualizar(Long id, Estudiante datos) {
        validar(datos);
        Estudiante existente = buscar(id);
        existente.setNombre(datos.getNombre());
        existente.setEmail(datos.getEmail());
        existente.setEdad(datos.getEdad());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Estudiante no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    public List<Estudiante> buscarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    private void validar(Estudiante e) {
        if (e.getNombre() == null || e.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (e.getEmail() == null || !e.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no es válido");
        }
        if (e.getEdad() == null || e.getEdad() < 0) {
            throw new IllegalArgumentException("La edad debe ser mayor o igual a 0");
        }
    }
}