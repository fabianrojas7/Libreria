package com.alura.literalura.service;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GutendexService gutendexService;

    public LibraryService(BookRepository bookRepository, AuthorRepository authorRepository, GutendexService gutendexService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.gutendexService = gutendexService;
    }

    @Transactional
    public boolean searchAndSaveBookByTitle(String title) {
        try {
            List<Book> books = gutendexService.searchBooksByTitle(title);
            if (!books.isEmpty()) {
                for (Book book : books) {
                    List<Author> managedAuthors = new ArrayList<>();
                    for (Author author : book.getAuthors()) {
                        Author savedAuthor = authorRepository.save(author); // JPA asignará el ID automáticamente
                        managedAuthors.add(savedAuthor);
                    }
                    book.setAuthors(managedAuthors);
                    bookRepository.save(book); // JPA asignará el ID automáticamente
                }
                System.out.println("Libros guardados exitosamente.");
                return true; // Se encontraron libros
            } else {
                System.out.println("No se encontraron libros con el título '" + title + "'.");
                return false; // No se encontraron libros
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el libro: " + e.getMessage());
            e.printStackTrace();
            return false; // Se produjo un error
        }
    }

    public void showStatistics() {
        long totalBooks = bookRepository.count();
        Book mostDownloadedBook = bookRepository.findFirstByOrderByDownloadCountDesc();
        Book leastDownloadedBook = bookRepository.findFirstByOrderByDownloadCountAsc();
        double averageDownloads = bookRepository.getAverageDownloads();

        System.out.println("\nEstadísticas de libros:");
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("Total de libros: " + df.format(totalBooks));
        System.out.println("Libro más descargado: " + mostDownloadedBook.getTitle() + " - Descargas: " + df.format(mostDownloadedBook.getDownloadCount()));
        System.out.println("Libro menos descargado: " + leastDownloadedBook.getTitle() + " - Descargas: " + df.format(leastDownloadedBook.getDownloadCount()));
        System.out.println("Media de descargas: " + String.format("%.2f", averageDownloads));
    }
}
