package com.uy.fibonacci.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.uy.fibonacci.service.FibonacciService.MAX_N;
import static com.uy.fibonacci.service.FibonacciService.MIN_N;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FibonacciCalculadoraTest {

    private FibonacciCalculadora calculadora;

    @BeforeEach
    void setUp() {
        calculadora = new FibonacciCalculadora();
    }

    // Casos base
    @Test
    @DisplayName("F(1) = 1")
    void testF1() {
        assertEquals(BigInteger.ONE, calculadora.calcular(1));
    }

    @Test
    @DisplayName("F(2) = 2")
    void testF2() {
        assertEquals(BigInteger.TWO, calculadora.calcular(2));
    }

    @Test
    @DisplayName("F(3) = 3")
    void testF3() {
        assertEquals(new BigInteger("3"), calculadora.calcular(3));
    }

    @Test
    @DisplayName("F(4) = 5")
    void testF4() {
        assertEquals(new BigInteger("5"), calculadora.calcular(4));
    }

    @Test
    @DisplayName("F(5) = 8")
    void testF5() {
        assertEquals(new BigInteger("8"), calculadora.calcular(5));
    }

    // Caso especifico 50
    @Test
    @DisplayName("F(50) = 20365011074")
    void testF50() {
        assertEquals(new BigInteger("20365011074"), calculadora.calcular(50));
    }

    // Iteracion
    @Test
    @DisplayName("Iteracion segun letra 1, 2, 3, 5, 8, 13, 21, 34...")
    void testPositivo() {
        int[] expected = {1, 2, 3, 5, 8, 13, 21, 34};
        for (int i = 0; i < expected.length; i++) {
            int n = i + 1;
            assertEquals(
                    new BigInteger(String.valueOf(expected[i])),
                    calculadora.calcular(n),
                    "F(" + n + ") debería ser " + expected[i]
            );
        }
    }

    // Casos borde (cero y negativos)
    @Test
    @DisplayName("F(0) No permitido")
    void testF0() {
        assertThatThrownBy(() -> calculadora.calcular(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N);
    }

    @Test
    @DisplayName("F(-1) No permitido")
    void testNegativo() {
        assertThatThrownBy(() -> calculadora.calcular(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N);
    }

    @Test
    @DisplayName("F(-100) No permitido")
    void testNegativo2() {
        assertThatThrownBy(() -> calculadora.calcular(-100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N);
    }

    // Limites del rango
    @Test
    @DisplayName("F(5000) numero grande > 1000 digitos")
    void testF5000() {
        BigInteger result = calculadora.calcular(5000);
        assertNotNull(result);
        assertTrue(result.toString().length() > 1000,
                "F(5000) debe tener mas de 1000 digitos");
    }
}
