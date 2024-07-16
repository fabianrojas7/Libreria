package com.alura.literalura.repository;

import com.alura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    //List<Book> findByTitleContainingIgnoreCase(String title);
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.authors WHERE UPPER(b.title) LIKE UPPER(CONCAT('%', :title, '%'))")
    List<Book> findByTitleContainingIgnoreCaseWithAuthors(@Param("title") String title);
    List<Book> findByLanguage(String language);
    Optional<Book> findByTitle(String title);

    @Query(value = "SELECT COUNT(*) FROM Book")
    long countTotalBooks();

    Book findFirstByOrderByDownloadCountDesc();

    Book findFirstByOrderByDownloadCountAsc();

    @Query(value = "SELECT AVG(b.downloadCount) FROM Book b")
    double getAverageDownloads();
}
