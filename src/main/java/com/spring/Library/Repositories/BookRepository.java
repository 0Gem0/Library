package com.spring.Library.Repositories;

import com.spring.Library.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByPersonIdIsNull();

    Optional<Book> findByNameStartingWith(String prefix);
}
