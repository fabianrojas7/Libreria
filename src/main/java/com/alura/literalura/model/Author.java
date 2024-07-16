package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int birthYear;
    private int deathYear;
    private String nationality;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

    // Constructores, getters y setters

    public Author() {}

    public Author(String name, int birthYear, int deathYear, String nationality) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.nationality = nationality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "****** DATOS DE AUTORES ******\n" +
                "Nombre = " + name + "\n" +
                "Año de Nacimiento = " + birthYear + "\n" +
                "Año de Defunción = " + deathYear + "\n";
    }
}