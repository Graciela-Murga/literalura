package com.alura.literalura.principal;

import com.alura.literalura.service.LibroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    private final LibroService libroService;
    private final Scanner teclado = new Scanner(System.in);

    public Principal(LibroService libroService) {
        this.libroService = libroService;
    }

    @Override
    public void run(String... args) {
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intenta de nuevo.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> libroService.listarLibrosRegistrados();
                case 3 -> libroService.listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> buscarLibrosPorTituloParcial();
                case 7 -> libroService.top10LibrosMasDescargados();
                case 8 -> libroService.estadisticas();
                case 0 -> System.out.println("Saliendo de la aplicación...");
                default -> System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("""
                
                ==== Menú LiterAlura ====
                1 - Buscar libro por título y guardarlo
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un año
                5 - Listar libros por idioma
                6 - Buscar libros en la BD por coincidencia parcial
                7 - Ver Top 10 libros más descargados
                8 - Ver estadísticas de la biblioteca
                0 - Salir
                Elige una opción:
                """);
    }

    private void buscarLibroPorTitulo() {
        System.out.print("Ingresa el título del libro a buscar: ");
        String titulo = teclado.nextLine();
        libroService.buscarYGuardarLibroPorTitulo(titulo);
    }

    private void listarAutoresVivos() {
        System.out.print("Ingresa el año: ");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            libroService.listarAutoresVivosEn(anio);
        } catch (NumberFormatException e) {
            System.out.println("Año inválido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Ingresa el idioma (EN, ES, PT, FR...): ");
        String idioma = teclado.nextLine();
        libroService.listarLibrosPorIdioma(idioma);
    }

    private void buscarLibrosPorTituloParcial() {
        System.out.print("Ingresa parte del título a buscar en la BD: ");
        String parcial = teclado.nextLine();
        libroService.buscarLibrosPorTituloParcial(parcial);
    }
}
