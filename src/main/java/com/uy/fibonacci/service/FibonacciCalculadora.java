package com.uy.fibonacci.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static com.uy.fibonacci.service.FibonacciService.MAX_N;
import static com.uy.fibonacci.service.FibonacciService.MIN_N;

@Component
public class FibonacciCalculadora {

    private static final Logger log = LoggerFactory.getLogger(FibonacciCalculadora.class);

    //Calcula el n positivo
    public BigInteger calcular(int n) {
        if (n >= 1) {
            log.info("Calculando Fibonacci POSITIVO({})", n);
            return calcularPositivo(n);
        }else{
            throw new IllegalArgumentException("Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N);
        }
    }

    // Para n >= 1: va desde F(1)=1, F(2)=2 sumando los dos anteriores
    private BigInteger calcularPositivo(int n) {
        if (n == 1) return BigInteger.ONE;
        if (n == 2) return BigInteger.TWO;

        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.TWO;

        for (int i = 3; i <= n; i++) {
            BigInteger siguiente = a.add(b);
            a = b;
            b = siguiente;
        }
        log.info("Calculado: Fibonacci({}) = {}", n, b);
        return b;
    }
}