package com.uy.fibonacci.service;

import com.uy.fibonacci.entity.Estadisticas;
import com.uy.fibonacci.repository.EstadisticasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstadisticasService {

    private static final Logger log = LoggerFactory.getLogger(EstadisticasService.class);

    private final EstadisticasRepository estadisticasRepository;

    public EstadisticasService(EstadisticasRepository estadisticasRepository) {
        this.estadisticasRepository = estadisticasRepository;
    }

    public void insertarSiNoExiste(int n) {
        try {
            insertarEstadistica(n);
        } catch (DataIntegrityViolationException e) {
            log.debug("Ya existe estadística para n={}", n);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertarEstadistica(int n) {
        estadisticasRepository.saveAndFlush(new Estadisticas(n));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementar(int n) {
        Estadisticas stats = estadisticasRepository.findByNForUpdate(n);
        estadisticasRepository.incrementa(n, LocalDateTime.now());

        log.info("contador = {}", stats.getContador());
    }

    // Devuelve los numeros mas consultados
    @Transactional(readOnly = true)
    public List<Estadisticas> getTopConsultados(int limite) {

        if (limite <= 0 || limite > 100) {
            throw new IllegalArgumentException(
                    "El limite debe estar entre 1 y 100"
            );
        }

        return estadisticasRepository.findTopByContador(
                PageRequest.of(0, limite)
        );
    }

    // Devuelve todas las estadísticas
    @Transactional(readOnly = true)
    public List<Estadisticas> getAllEstadisticas() {
        log.info("Devolviendo todas las estadisticas");
        return estadisticasRepository.findAll();
    }
}