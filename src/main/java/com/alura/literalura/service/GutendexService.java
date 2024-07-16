package com.alura.literalura.service;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GutendexService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GutendexService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Book> searchBooksByTitle(String title) throws Exception {
        String url = "https://gutendex.com/books?search=" + title.toLowerCase();
        String jsonResponse = restTemplate.getForObject(url, String.class);

        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultsNode = rootNode.path("results");

        List<Book> books = new ArrayList<>();
        for (JsonNode bookNode : resultsNode) {
            String bookTitle = bookNode.path("title").asText();
            String language = bookNode.path("languages").get(0).asText();
            int downloadCount = bookNode.path("download_count").asInt();

            List<Author> authors = new ArrayList<>();
            for (JsonNode authorNode : bookNode.path("authors")) {
                String authorName = authorNode.path("name").asText();
                int birthYear = authorNode.path("birth_year").isMissingNode() ? 0 : authorNode.path("birth_year").asInt();
                int deathYear = authorNode.path("death_year").isMissingNode() ? 0 : authorNode.path("death_year").asInt();
                Author author = new Author(authorName, birthYear, deathYear, null);
                authors.add(author);
            }

            Book book = new Book(bookTitle, language, downloadCount, authors);
            books.add(book);
        }

        return books;
    }
}
