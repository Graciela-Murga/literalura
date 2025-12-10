package com.alura.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConversorJson {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T convertir(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JSON: " + e.getMessage(), e);
        }
    }
}
