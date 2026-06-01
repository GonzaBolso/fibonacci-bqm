package com.uy.fibonacci.controller;


import com.uy.fibonacci.repository.FibonacciRepository;
import com.uy.fibonacci.service.FibonacciService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FibonacciController.class)
class FibonacciControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FibonacciService fibonacciService;

    @MockBean
    private FibonacciRepository fibonacciRepository;

    @Test
    @DisplayName("/fibonacci?n=50")
    void testGetFibonacci50() throws Exception {
        when(fibonacciRepository.existsById(50)).thenReturn(false);
        when(fibonacciService.getFibonacci(50)).thenReturn(new BigInteger("20365011074"));

        mockMvc.perform(get("/fibonacci").param("n", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.n").value(50))
                .andExpect(jsonPath("$.resultado").value("20365011074"))
                .andExpect(jsonPath("$.enCache").value(false));
    }

    @Test
    @DisplayName("/fibonacci?n=abc")
    void testCaracter() throws Exception {
        mockMvc.perform(get("/fibonacci").param("n", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("/fibonacci?n=5001")
    void testRango() throws Exception {
        when(fibonacciService.estaEnBD(5001)).thenReturn(false);
        when(fibonacciService.getFibonacci(5001))
                .thenThrow(new IllegalArgumentException("El valor de n debe estar entre 1 y 5000"));

        mockMvc.perform(get("/fibonacci").param("n", "5001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("/fibonacci?n=0")
    void testN0() throws Exception {
        when(fibonacciService.estaEnBD(0)).thenReturn(false);
        when(fibonacciService.getFibonacci(0)).
                thenThrow(new IllegalArgumentException("El valor de n debe estar entre 1 y 5000"));

        mockMvc.perform(get("/fibonacci").param("n", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("la primera vez el Cache es false si esta en BD")
    void testEnCache() throws Exception {
        when(fibonacciService.estaEnBD(10)).thenReturn(false);
        when(fibonacciService.getFibonacci(10)).thenReturn(new BigInteger("89"));

        mockMvc.perform(get("/fibonacci").param("n", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enCache").value(false));
    }
}
