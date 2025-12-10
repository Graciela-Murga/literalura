LiterAlura – Catálogo literario con Java, Spring Boot y PostgreSQL
Challenge Back-End – Oracle Next Education + Alura Latam

Descripción del Proyecto

LiterAlura es una aplicación de consola desarrollada en Java + Spring Boot que permite:

-Buscar libros por título en la API Gutendex
-Guardar libros y autores en una base de datos PostgreSQL
-Listar libros registrados
-Listar autores registrados
-Consultar qué autores estaban vivos en un año determinado
-Filtrar libros por idioma
-Evitar duplicados (no guarda un libro dos veces)

Este proyecto forma parte del Challenge Back-End del programa ONE (Oracle + Alura Latam).

Tecnologías utilizadas

Backend

-Java 25
-Spring Boot 4.0.0
-Spring Data JPA
-PostgreSQL Driver
-Hibernate ORM

Herramientas

-Maven
-PostgreSQL 16 + PgAdmin
-IntelliJ IDEA
-Git & GitHub

API utilizada

Este proyecto consume la API pública Gutendex:
https://gutendex.com/books/

Se realiza una búsqueda por título y se mapear la respuesta a DTOs personalizados.

Cómo ejecutar el proyecto

1️- Clona el repositorio
git clone https://github.com/Graciela-Murga/literalura.git

2️- Configura PostgreSQL

En PgAdmin crea una base llamada:
literalura

3️- Verifica credenciales en application.properties
4️- Ejecuta la aplicación desde IntelliJ o con Maven
mvn spring-boot:run

Autora
Graciela Murga
Desarrolladora Back-End en formación – Programa ONE (Oracle + Alura)

GitHub: https://github.com/Graciela-Murga

