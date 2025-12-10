package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroGutendexDto(
        String title,
        List<AutorGutendexDto> authors,
        List<String> languages,
        @JsonProperty("download_count") Integer downloadCount
) {}
