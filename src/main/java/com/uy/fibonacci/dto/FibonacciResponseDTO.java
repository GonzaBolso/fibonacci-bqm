package com.uy.fibonacci.dto;

import java.math.BigInteger;

// DTO para las respuestas de la API
// String para los json
public class FibonacciResponseDTO {
    private int n;
    private String resultado;
    private boolean enCache;
    private boolean enBD;

    public FibonacciResponseDTO(int n, BigInteger resultado, boolean enCache, boolean enBd) {
        this.n = n;
        this.resultado = resultado.toString();
        this.enCache = enCache;
        this.enBD = enBd;
    }

    public int getN() { return n; }
    public String getResultado() { return resultado; }
    public boolean getEnCache() { return enCache; }
    public boolean getEnBD() { return enBD; }

    public void setN(int n) {
        this.n = n;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public void setEnCache(boolean enCache) {
        this.enCache = enCache;
    }
    public void setEnBD(boolean enBD) { this.enBD = enBD;}
}