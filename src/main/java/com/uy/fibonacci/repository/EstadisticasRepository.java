package com.uy.fibonacci.repository;

import com.uy.fibonacci.entity.Estadisticas;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EstadisticasRepository extends JpaRepository<Estadisticas, Integer> {

    // en JPQL - trae los N valores mas consultados, ordenados de mayor a menor
    @Query("SELECT e FROM Estadisticas e ORDER BY e.contador DESC")
    List<Estadisticas> findTopByContador(Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Estadisticas q SET q.contador = q.contador + 1, q.fecha_ultima_consulta = :now WHERE q.n = :n")
    int incrementa(int n, LocalDateTime now);

    @Query("SELECT e FROM Estadisticas e WHERE e.n = :n")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Estadisticas findByNForUpdate(@Param("n") int n);

}