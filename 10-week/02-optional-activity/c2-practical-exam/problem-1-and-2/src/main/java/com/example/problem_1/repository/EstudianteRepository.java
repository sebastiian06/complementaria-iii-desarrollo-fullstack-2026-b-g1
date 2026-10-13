package com.example.problem_1.repository;

import com.example.problem_1.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // Query method: Spring genera el SQL automáticamente
    List<Estudiante> findByNombre(String nombre);

    // Otro ejemplo: buscar por email
    Estudiante findByEmail(String email);
}