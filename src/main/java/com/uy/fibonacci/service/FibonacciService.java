package com.uy.fibonacci.service;


import com.uy.fibonacci.entity.Fibonacci;
import com.uy.fibonacci.repository.EstadisticasRepository;
import com.uy.fibonacci.repository.FibonacciRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class FibonacciService {

    public static final int MAX_N = 5000;
    public static final int MIN_N = 1;
    private static final Logger log = LoggerFactory.getLogger(FibonacciService.class);

    // Cache en memoria
    private final ConcurrentHashMap<Integer, BigInteger> memoriaCache = new ConcurrentHashMap<>();

    private final FibonacciRepository fibonacciRepository;
    private final EstadisticasRepository estadisticasRepository;
    private final FibonacciCalculadora calculadora;
    private final EstadisticasService estadisticasService;

    public FibonacciService(
            FibonacciRepository fibonacciRepository,
            EstadisticasRepository estadisticasRepository,
            FibonacciCalculadora calculadora,
            EstadisticasService estadisticasService) {
        this.fibonacciRepository = fibonacciRepository;
        this.estadisticasRepository = estadisticasRepository;
        this.calculadora = calculadora;
        this.estadisticasService = estadisticasService;
    }

    public BigInteger getFibonacci(int n) {
        validarN(n);
        estadisticasService.insertarSiNoExiste(n);
        estadisticasService.incrementar(n);
        return buscarOCalcular(n);
    }

    private void validarN(int n) {
        if (n > MAX_N || n < MIN_N) {
            log.warn("Valor {} no esta entre {} y {}", n, MIN_N, MAX_N);
            throw new IllegalArgumentException(
                    "Por definicion, el valor de n debe ser un numero positivo entre " + MIN_N + " y " + MAX_N);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private BigInteger buscarOCalcular(int n) {

        // busca en memoria
        BigInteger cacheado;
        if (estaEnCache(n)) {
            log.info("Valor {} en memoria, lo devuelvo", n);
            return memoriaCache.get(n);
        }

        // buscar en BD
        if (estaEnBD(n)) {
            log.info("Valor {} en BD, lo busco en Base", n);

            Optional<Fibonacci> enBd = fibonacciRepository.findById(n);
            BigInteger resultadoDevuelto = enBd.get().getResultado();

            memoriaCache.put(n, resultadoDevuelto);
            log.info("Valor {} agregado en memoria", n);
            // log.info("PRUEBA Valores en memoria: {}", resultadoDevuelto);

            return resultadoDevuelto;
        }

        // Si no existe, calcular y guardar en los dos
        log.info("Valor {} no encontrado en memoria ni BD, lo calculo", n);
        BigInteger resultadoCalculado = calculadora.calcular(n);

        try {
            fibonacciRepository.saveAndFlush(new Fibonacci(n, resultadoCalculado));
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicado. Registro ya insertado");

            return fibonacciRepository.findById(n)
                    .orElseThrow()
                    .getResultado();
        }

        memoriaCache.put(n, resultadoCalculado);
        log.info("Valores en memoria: {}", memoriaCache);

        return resultadoCalculado;
    }

    public boolean estaEnBD(int n) {
        return fibonacciRepository.existsById(n);
    }

    public boolean estaEnCache(int n) {
        return memoriaCache.containsKey(n);
    }
}
