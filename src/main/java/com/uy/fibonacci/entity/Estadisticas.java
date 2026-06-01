package com.uy.fibonacci.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "estadisticas")
public class Estadisticas {
    @Id
    @Column(name = "n", nullable = false, unique = true)
    private Integer n;

    @Column(name = "contador", nullable = false)
    private Long contador;

    @Column(name = "fecha_primer_consulta", nullable = false)
    private LocalDateTime fecha_primer_consulta;

    @Column(name = "fecha_ultima_consulta", nullable = false)
    private LocalDateTime fecha_ultima_consulta;

    public Estadisticas() {}

    public Estadisticas(Integer n) {
        this.n = n;
        this.contador = 0L;
        this.fecha_primer_consulta = LocalDateTime.now();
        this.fecha_ultima_consulta = LocalDateTime.now();
    }

    public void incrementa() {
        this.contador++;
        this.fecha_ultima_consulta = LocalDateTime.now();
    }

    public Integer getN() { return n; }
    public Long getContador() { return contador; }
    public LocalDateTime getFecha_primer_consulta() { return fecha_primer_consulta; }
    public LocalDateTime getFecha_ultima_consulta() { return fecha_ultima_consulta; }

    public void setN(Integer n) {
        this.n = n;
    }
    public void setContador(Long contador) {
        this.contador = contador;
    }
    public void setFecha_primer_consulta(LocalDateTime fecha_primer_consulta) {
        this.fecha_primer_consulta = fecha_primer_consulta;
    }
    public void setFecha_ultima_consulta(LocalDateTime fecha_ultima_consulta) {
        this.fecha_ultima_consulta = fecha_ultima_consulta;
    }
}
