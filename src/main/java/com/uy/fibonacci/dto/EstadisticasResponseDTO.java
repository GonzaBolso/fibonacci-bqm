package com.uy.fibonacci.dto;

//DTO para las estadisticas de la API

import com.uy.fibonacci.entity.Estadisticas;

import java.time.LocalDateTime;

public class EstadisticasResponseDTO {
    private int n;
    private long contador;
    private LocalDateTime fecha_primer_consulta;
    private LocalDateTime fecha_ultima_consulta;

    public EstadisticasResponseDTO(Estadisticas estadistica) {
        this.n = estadistica.getN();
        this.contador = estadistica.getContador();
        this.fecha_primer_consulta = estadistica.getFecha_primer_consulta();
        this.fecha_ultima_consulta = estadistica.getFecha_ultima_consulta();
    }

    public int getN() { return n; }
    public long getContador() { return contador; }
    public LocalDateTime getFecha_primer_consulta() {
        return fecha_primer_consulta;
    }
    public LocalDateTime getFecha_ultima_consulta() {
        return fecha_ultima_consulta;
    }

    public void setN(int n) {
        this.n = n;
    }
    public void setContador(long contador) {
        this.contador = contador;
    }
    public void setFecha_primer_consulta(LocalDateTime fecha_primer_consulta) {
        this.fecha_primer_consulta = fecha_primer_consulta;
    }
    public void setFecha_ultima_consulta(LocalDateTime fecha_ultima_consulta) {
        this.fecha_ultima_consulta = fecha_ultima_consulta;
    }
}
