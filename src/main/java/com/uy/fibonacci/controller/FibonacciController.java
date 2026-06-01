package com.uy.fibonacci.controller;


import com.uy.fibonacci.dto.FibonacciResponseDTO;
import com.uy.fibonacci.repository.FibonacciRepository;
import com.uy.fibonacci.service.FibonacciService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

// REST
@RestController
@RequestMapping("/fibonacci")
public class FibonacciController {

    private final FibonacciService fibonacciService;

    public FibonacciController(FibonacciService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @GetMapping
    public ResponseEntity<FibonacciResponseDTO> getFibonacci(@RequestParam int n) {
        boolean estaEnBD = fibonacciService.estaEnBD(n);
        boolean estaEnCache = fibonacciService.estaEnCache(n);

        BigInteger result = fibonacciService.getFibonacci(n);

        return ResponseEntity.ok(new FibonacciResponseDTO(n, result, estaEnCache, estaEnBD));
    }
}
