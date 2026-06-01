package com.uy.fibonacci.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
public class Fibonacci {
    //public static final int PENDIENTE = -1;
    //public static final int CALCULANDO = 0;
    //public static final int CALCULADO = 1;

    @Id
    @Column(name = "n", nullable = false, unique = true)
    private Integer n;

    @Column(name = "resultado", nullable = false, columnDefinition = "TEXT")
    private String resultado;

    @Column(name = "fecha_calculado", nullable = false)
    private LocalDateTime fechaCalculado;

    //@Column(name = "calculado", nullable = false)
    //private Integer calculado;

    public Fibonacci() {}

    public Fibonacci(Integer n, BigInteger resultado) {
        this.n = n;
        this.resultado = resultado.toString();
        this.fechaCalculado = LocalDateTime.now();
    }

    public Integer getN() { return n; }
    public BigInteger getResultado() {
        return new BigInteger(resultado);
    }
    public LocalDateTime getFechaCalculado() { return fechaCalculado; }

    public void setN(Integer n) {
        this.n = n;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public void setFechaCalculado(LocalDateTime fechaCalculado) {
        this.fechaCalculado = fechaCalculado;
    }
}
