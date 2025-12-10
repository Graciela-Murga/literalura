package com.alura.literalura.service;

import com.alura.literalura.dto.LibroGutendexDto;
import com.alura.literalura.dto.ResultadoGutendexDto;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class LibroService {

    private static final String URL_GUTENDEX = "https://gutendex.com/books/?search=";

    private final ConsumoApi consumoApi;
    private final ConversorJson conversorJson;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(ConsumoApi consumoApi,
                        ConversorJson conversorJson,
                        LibroRepository libroRepository,
                        AutorRepository autorRepository) {
        this.consumoApi = consumoApi;
        this.conversorJson = conversorJson;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void buscarYGuardarLibroPorTitulo(String tituloBuscado) {
        String url = URL_GUTENDEX + URLEncoder.encode(tituloBuscado, StandardCharsets.UTF_8);
        String json = consumoApi.obtenerDatos(url);

        ResultadoGutendexDto resultado =
                conversorJson.convertir(json, ResultadoGutendexDto.class);

        if (resultado.results() == null || resultado.results().isEmpty()) {
            System.out.println("El libro no fue encontrado en la API.");
            return;
        }

        LibroGutendexDto dto = resultado.results().get(0);

        if (libroRepository.existsByTituloIgnoreCase(dto.title())) {
            System.out.println("El libro ya está registrado en la base de datos.");
            return;
        }

        Autor autor = null;
        if (dto.authors() != null && !dto.authors().isEmpty()) {
            var autorDto = dto.authors().get(0);
            String nombreAutor = autorDto.name();

            Optional<Autor> autorExistente = autorRepository.findByNombreIgnoreCase(nombreAutor);
            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
            } else {
                autor = new Autor(
                        nombreAutor,
                        autorDto.birthYear(),
                        autorDto.deathYear()
                );
                autorRepository.save(autor);
            }
        }

        String idioma = "desconocido";
        if (dto.languages() != null && !dto.languages().isEmpty()) {
            idioma = dto.languages().get(0);
        }

        Libro libro = new Libro(
                dto.title(),
                idioma,
                dto.downloadCount(),
                autor
        );

        libroRepository.save(libro);

        System.out.println("Libro guardado con éxito:");
        System.out.println(libro);
    }

    public void listarLibrosRegistrados() {
        var libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    public void listarAutoresRegistrados() {
        var autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    public void listarAutoresVivosEn(Integer anio) {
        var autores = autorRepository.buscarAutoresVivosEn(anio);
        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("Autores vivos en " + anio + ":");
            autores.forEach(a -> System.out.println(a.getNombre()));
        }
    }

    public void listarLibrosPorIdioma(String idioma) {
        var libros = libroRepository.findByIdiomaIgnoreCase(idioma);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + idioma);
        } else {
            System.out.println("Libros en idioma " + idioma + ":");
            libros.forEach(l -> System.out.println(l.getTitulo() +
                    " - " + (l.getAutor() != null ? l.getAutor().getNombre() : "Autor desconocido")));
        }
    }

    public void buscarLibrosPorTituloParcial(String parcial) {
        var libros = libroRepository.findByTituloContainingIgnoreCase(parcial);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros que contengan: " + parcial);
        } else {
            System.out.println("Libros que contienen '" + parcial + "':");
            libros.forEach(System.out::println);
        }
    }

    public void top10LibrosMasDescargados() {
        var libros = libroRepository.findTop10ByOrderByDescargasDesc();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados aún.");
        } else {
            System.out.println("Top 10 libros más descargados:");
            libros.forEach(l -> System.out.println(
                    l.getTitulo() + " - " + l.getDescargas() + " descargas"
            ));
        }
    }

    public void estadisticas() {
        long totalLibros = libroRepository.count();
        long totalAutores = autorRepository.count();

        System.out.println(" Estadísticas de la biblioteca:");
        System.out.println("Total de libros registrados: " + totalLibros);
        System.out.println("Total de autores registrados: " + totalAutores);

        var topLibroOpt = libroRepository.findTop10ByOrderByDescargasDesc()
                .stream().findFirst();

        topLibroOpt.ifPresent(l -> {
            System.out.println("Libro más descargado: " + l.getTitulo() +
                    " (" + l.getDescargas() + " descargas)");
        });


        var libros = libroRepository.findAll();
        if (!libros.isEmpty()) {
            var idiomaMasComun = libros.stream()
                    .map(Libro::getIdioma)
                    .filter(id -> id != null && !id.isBlank())
                    .collect(java.util.stream.Collectors.groupingBy(
                            id -> id,
                            java.util.stream.Collectors.counting()
                    ))
                    .entrySet()
                    .stream()
                    .max(java.util.Map.Entry.comparingByValue())
                    .map(java.util.Map.Entry::getKey)
                    .orElse(null);

            if (idiomaMasComun != null) {
                System.out.println("Idioma más común: " + idiomaMasComun);
            }
        }
    }
}
