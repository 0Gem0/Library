package com.spring.Library.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "person")
    private List<Book> books;

    @Column(name = "birth_year")
    @Min(value = 1900,message = "Must not be empty")
    private int birthYear;

    public Person(int id, String fullName, List<Book> books, int birthYear) {
        this.id = id;
        this.fullName = fullName;
        this.books = books;
        this.birthYear = birthYear;
    }

    public Person() {
    }
}
