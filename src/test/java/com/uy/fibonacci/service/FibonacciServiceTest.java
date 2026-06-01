package com.uy.fibonacci.service;

import com.uy.fibonacci.entity.Fibonacci;
import com.uy.fibonacci.repository.EstadisticasRepository;
import com.uy.fibonacci.repository.FibonacciRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static com.uy.fibonacci.service.FibonacciService.MAX_N;
import static com.uy.fibonacci.service.FibonacciService.MIN_N;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FibonacciServiceTest {

    @Mock
    private FibonacciRepository fibonacciRepository;

    @Mock
    private EstadisticasRepository estadisticasRepository;

    @Mock
    private EstadisticasService estadisticasService;

    private FibonacciCalculadora calculadora;

    private FibonacciService fibonacciService;



    @BeforeEach
    void setUp() {
        calculadora = new FibonacciCalculadora();
        fibonacciService = new FibonacciService(
                fibonacciRepository,
                estadisticasRepository,
                calculadora,
                estadisticasService);
        //fibonacciService.limpiarCache();
    }

    @Test
    @DisplayName("Existe n en base")
    void testEnBD() {
        Fibonacci enBase = new Fibonacci(5, new BigInteger("8"));
        when(fibonacciRepository.existsById(5)).thenReturn(true);
        when(fibonacciRepository.findById(5)).thenReturn(Optional.of(enBase));
        doNothing().when(estadisticasService).insertarSiNoExiste(5);
        doNothing().when(estadisticasService).incrementar(5);

        BigInteger resultado = fibonacciService.getFibonacci(5);

        assertEquals(new BigInteger("8"), resultado);
        verify(fibonacciRepository, never()).saveAndFlush(any(Fibonacci.class));
    }

    @Test
    @DisplayName("Después de consultar, queda en memoria")
    void testQuedaEnMemoria() {
        Fibonacci enBd = new Fibonacci(5, new BigInteger("8"));
        when(fibonacciRepository.existsById(5)).thenReturn(true);
        when(fibonacciRepository.findById(5)).thenReturn(Optional.of(enBd));

        doNothing().when(estadisticasService).insertarSiNoExiste(5);
        doNothing().when(estadisticasService).incrementar(5);

        assertFalse(fibonacciService.estaEnCache(5));

        fibonacciService.getFibonacci(5);

        assertTrue(fibonacciService.estaEnCache(5));
    }

    @Test
    @DisplayName("Cuando n NO existe, calcula y guarda en BD")
    void testNoCacheado() {
        BigInteger resultado = fibonacciService.getFibonacci(5);

        assertEquals(new BigInteger("8"), resultado);
        verify(fibonacciRepository, times(1)).saveAndFlush(any(Fibonacci.class));
    }

    @Test
    @DisplayName("F(50) devuelve 20365011074")
    void testF50() {
        BigInteger resultado = fibonacciService.getFibonacci(50);

        assertEquals(new BigInteger("20365011074"), resultado);
    }

    @Test
    @DisplayName("n > 5000 lanza IllegalArgumentException")
    void testRango() {
        assertThrows(IllegalArgumentException.class,
                () -> fibonacciService.getFibonacci(5001));
    }

    @Test
    @DisplayName("n = 5000 OK")
    void test5000() {
        assertDoesNotThrow(() -> fibonacciService.getFibonacci(5000));
    }

    @Test
    @DisplayName("excepcion si n es negativo")
    void testNegativo() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fibonacciService.getFibonacci(-1)
        );

        assertEquals(
                "Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("excepcion si n es negativo")
    void testCero() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fibonacciService.getFibonacci(0)
        );

        assertEquals(
                "Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Primera consulta se crea e incrementa")
    void testCrearEstadisticaEnPrimerConsulta() {
        fibonacciService.getFibonacci(5);

        verify(estadisticasService, times(1)).insertarSiNoExiste(5);
        verify(estadisticasService, times(1)).incrementar(5);
    }

}
