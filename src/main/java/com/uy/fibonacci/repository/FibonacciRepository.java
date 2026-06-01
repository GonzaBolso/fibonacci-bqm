package com.uy.fibonacci.repository;

import com.uy.fibonacci.entity.Fibonacci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// findById(n) -> busca por clave primaria
// save(entity) -> inserta o actualiza
// estaEnBD(n) -> true si existe
// findAll() -> todos los registros
@Repository
public interface FibonacciRepository extends JpaRepository<Fibonacci, Integer> {
}