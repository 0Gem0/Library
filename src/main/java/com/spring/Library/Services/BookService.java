package com.spring.Library.Services;

import com.spring.Library.Model.Book;
import com.spring.Library.Model.Person;
import com.spring.Library.Repositories.BookRepository;
import com.spring.Library.Repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private PeopleRepository peopleRepository;


    @Autowired
    public BookService(BookRepository bookRepository, PeopleRepository peopleRepository) {
        this.bookRepository = bookRepository;
        this.peopleRepository = peopleRepository;
    }



    public List<Book> findAll(Boolean isSorted, Integer page, Integer booksPerPage){
        if (isSorted != null && isSorted && page != null && booksPerPage != null){
            return bookRepository.findAll(PageRequest.of(page,booksPerPage,Sort.by("year"))).getContent();
        }
        else if (isSorted != null && isSorted){
            return bookRepository.findAll(Sort.by("year"));
        }
        else if(page != null & booksPerPage != null){
            return bookRepository.findAll(PageRequest.of(page,booksPerPage)).getContent();
        }
        else {
            return bookRepository.findAll();
        }
    }

    public Book findByName(String prefix){
        Optional<Book> book = bookRepository.findByNameStartingWith(prefix);
        if (book.isPresent()){
            long days = ChronoUnit.DAYS.between(book.get().getDate_issue(), LocalDateTime.now());
            book.get().setOverdue(days >= 10);
            return book.get();
        }
        else
            return new Book();
    }
    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    public Book findById(int id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            long days = ChronoUnit.DAYS.between(book.get().getDate_issue(), LocalDateTime.now());
            book.get().setOverdue(days >= 10);
            return book.get();
        }
        else
            return null;
    }
    @Transactional
    public void addPerson(int id, int personId){
        Book book = findById(id);
        book.setDate_issue(LocalDateTime.now());
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()){
            book.setPerson(person.get());
        }
        else {
            book.setPerson(null);
        }

    }
    @Transactional
    public void deletePerson(int id){
        Book book = findById(id);
        book.setPerson(null);
    }
    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }
    @Transactional
    public void updateById(int id, Book book){
        Book bookToBeUpdated = findById(id);
        bookToBeUpdated.setAuthor(book.getAuthor());
        bookToBeUpdated.setName(book.getName());
        bookToBeUpdated.setYear(book.getYear());
    }

    public List<Book> findAvailableBooks(){
        List<Book> books = bookRepository.findByPersonIdIsNull();
        return bookRepository.findByPersonIdIsNull();
    }
}
