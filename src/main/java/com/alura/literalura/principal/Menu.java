package com.alura.literalura.principal;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public Menu(LibraryService libraryService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.libraryService = libraryService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void displayMenu() {
        System.out.println("\nSeleccione una opción del menú:");
        System.out.println("1 - Buscar libro por título");
        System.out.println("2 - Listar libros registrados");
        System.out.println("3 - Listar autores registrados");
        System.out.println("4 - Listar autores vivos en un determinado año");
        System.out.println("5 - Listar libros por idioma");
        System.out.println("6 - Estadísticas");
        System.out.println("0 - Salir\n");
    }
    //Scanner scanner = new Scanner(System.in);
    public boolean handleOption(int option, Scanner scanner) {
        // Implementa la lógica para manejar cada opción del menú
        switch (option) {
            case 1:
                buscarLibroPorTitulo(scanner);
                break;
            case 2:
                listarLibrosRegistrados();
                break;
            case 3:
                listarAutoresRegistrados();
                break;
            case 4:
                listarAutoresVivosEnAnio(scanner);
                break;
            case 5:
                listarLibrosPorIdioma(scanner);
                break;
            case 6:
                mostrarEstadisticas();
                break;
            case 0:
                System.out.println("Saliendo...");
                System.exit(0);
            default:
                System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
        }
        return true; // Indica que el menú debe seguir mostrándose
    }

    private void buscarLibroPorTitulo(Scanner scanner) {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        String nombreLibro = scanner.nextLine();
        libraryService.searchAndSaveBookByTitle(nombreLibro);
    }

    private void listarLibrosRegistrados() {
        System.out.println("Ingrese el nombre del libro a buscar:");
        Scanner scanner = new Scanner(System.in);
        String titulo = scanner.nextLine();
        List<Book> books = bookRepository.findByTitleContainingIgnoreCaseWithAuthors(titulo);
        if (books.isEmpty()) {
            System.out.println("No se encontraron libros con ese nombre.");
        } else {
            for (Book book : books) {
                System.out.println("\n****** DATOS DEL LIBRO ******");
                System.out.println("Título: " + book.getTitle());
                System.out.println("Idioma: " + book.getLanguage());
                System.out.println("Cantidad de descargas: " + book.getDownloadCount());
                //System.out.println("Autores:");
                for (Author author : book.getAuthors()) {
                    System.out.println("Autor: " + author.getName());
                }
            }
        }
    }


    private void listarAutoresRegistrados() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("No se encontraron autores registrados.");
        } else {
            authors.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosEnAnio(Scanner scanner) {
        System.out.println("Ingrese el año:");
        int anio = scanner.nextInt();
        List<Author> authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(anio, anio);
        if (authors.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
        } else {
            authors.forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdioma(Scanner scanner) {
        System.out.println("Ingrese el idioma (es - para español, en - para inglés, fr - para francés, pt - para portugués):");
        String idioma = scanner.nextLine();
        List<Book> books = bookRepository.findByLanguage(idioma);
        if (books.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void mostrarEstadisticas() {
        libraryService.showStatistics();
    }
}