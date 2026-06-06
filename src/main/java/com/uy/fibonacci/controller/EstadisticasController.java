package com.uy.fibonacci.controller;

import com.uy.fibonacci.dto.EstadisticasResponseDTO;
import com.uy.fibonacci.service.EstadisticasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//REST
// GET /estadisticas/top?limit=10
// GET /estadisticas/all

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    // Devuelve los N números de Fibonacci más consultados.
    @GetMapping("/top")
    public ResponseEntity<List<EstadisticasResponseDTO>> getTop(
    @RequestParam(name = "limite", defaultValue = "10") int limite) {

        List<EstadisticasResponseDTO> response =
                estadisticasService.getTopConsultados(limite)
                        .stream()
                        .map(EstadisticasResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // /estadisticas/all
    // Devuelve estadísticas de todos los valores consultados.
    @GetMapping("/all")
    public ResponseEntity<List<EstadisticasResponseDTO>> getAll() {
        List<EstadisticasResponseDTO> response = estadisticasService.getAllEstadisticas()
                .stream()
                .map(EstadisticasResponseDTO::new)
                .toList();

        return ResponseEntity.ok(response);
    }
}