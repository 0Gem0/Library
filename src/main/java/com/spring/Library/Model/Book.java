package com.spring.Library.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Should not be empty")
    @Column(name = "author")
    private String author;

    @NotEmpty(message = "Should not be empty")
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Min(value = 1000 , message = "Not valid age")
    @Column(name = "year")
    private int year;

    @Column(name = "date_issue")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date_issue;

    @Transient
    boolean isOverdue;

    public Book(int id, String author, String name, int year) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.year = year;
    }

    public Book() {
    }
}
