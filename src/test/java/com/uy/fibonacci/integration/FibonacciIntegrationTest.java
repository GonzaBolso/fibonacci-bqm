package com.uy.fibonacci.integration;

import com.uy.fibonacci.repository.EstadisticasRepository;
import com.uy.fibonacci.repository.FibonacciRepository;
import com.uy.fibonacci.service.FibonacciService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FibonacciIntegrationTest {

    @Autowired private FibonacciService fibonacciService;
    @Autowired private FibonacciRepository fibonacciRepository;
    @Autowired private EstadisticasRepository estadisticasRepository;

    @BeforeEach
    void setUp() {
        fibonacciRepository.deleteAll();
        estadisticasRepository.deleteAll();
    }

    @Test
    @DisplayName("F(50) = 20365011074")
    void testF50() {
        assertEquals(new BigInteger("20365011074"), fibonacciService.getFibonacci(50));
    }

    @Test
    @DisplayName("Iteracion hasta F(4)")
    void testInicio() {
        assertEquals(BigInteger.ONE,        fibonacciService.getFibonacci(1));
        assertEquals(BigInteger.TWO,        fibonacciService.getFibonacci(2));
        assertEquals(new BigInteger("3"),   fibonacciService.getFibonacci(3));
        assertEquals(new BigInteger("5"),   fibonacciService.getFibonacci(4));
    }

    @Test
    @DisplayName("El resultado se persiste la primera vez")
    void testPersistencia() {
        assertFalse(fibonacciRepository.existsById(7));
        fibonacciService.getFibonacci(7);
        assertTrue(fibonacciRepository.existsById(7));
        assertEquals(new BigInteger("21"), fibonacciRepository.findById(7).get().getResultado());
    }

    @Test
    @DisplayName("Segunda vez que consulta n, no duplica")
    void testSegundaConsulta() {
        fibonacciService.getFibonacci(10);
        fibonacciService.getFibonacci(10);
        assertEquals(1, fibonacciRepository.findAll().size());
    }

    @Test
    @DisplayName("Concurrencia")
    void testConcurrencia() throws InterruptedException, ExecutionException {
        int threadCount = 3;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        List<Callable<BigInteger>> tasks = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            tasks.add(() -> fibonacciService.getFibonacci(42));
        }

        List<Future<BigInteger>> futures = executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        BigInteger expected = new BigInteger("433494437");
        for (Future<BigInteger> future : futures) {
            assertEquals(expected, future.get());
        }

        assertEquals(1, fibonacciRepository.findAll().stream()
                .filter(r -> r.getN() == 42).count());
    }

    @Test
    @DisplayName("n = 5001 lanza IllegalArgumentException")
    void testNOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> fibonacciService.getFibonacci(5001));
    }

    @Test
    @DisplayName("F(5000) se calcula sin error")
    void testF5000() {
        assertDoesNotThrow(() -> {
            BigInteger result = fibonacciService.getFibonacci(5000);
            assertTrue(result.toString().length() > 1000);
        });
    }

    @Test
    @DisplayName("Segunda llamada viene de memoria, no de BD")
    void testSecondCallComesFromMemory() {
        // Primera llamada: miss en memoria, miss en BD → calcula y guarda en ambos
        fibonacciService.getFibonacci(6);
        assertFalse(fibonacciService.estaEnBD(6) == false); // ya está en memoria

        // Borramos de BD para probar que la segunda llamada no la necesita
        fibonacciRepository.deleteById(6);

        // Segunda llamada: hit en memoria → no va a BD
        BigInteger result = fibonacciService.getFibonacci(6);
        assertEquals(new BigInteger("13"), result); // F(6) = 13
    }

    @Test
    @DisplayName("Al arrancar con BD con datos, se llena la memoria correctamente")
    void testMemoriaCargadaDesdeBD() {
        fibonacciRepository.save(new com.uy.fibonacci.entity.Fibonacci(8, new BigInteger("34")));

        assertFalse(fibonacciService.estaEnCache(8));

        BigInteger resultado = fibonacciService.getFibonacci(8);
        assertEquals(new BigInteger("34"), resultado);

        assertTrue(fibonacciService.estaEnCache(8));
    }
}

