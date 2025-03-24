package com.spring.Library.Services;

import com.spring.Library.Model.Book;
import com.spring.Library.Model.Person;
import com.spring.Library.Repositories.BookRepository;
import com.spring.Library.Repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private PeopleRepository peopleRepository;

    private BookRepository bookRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BookRepository bookRepository) {
        this.peopleRepository = peopleRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll(){
         return peopleRepository.findAll();
    }
    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    public Person findById(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());
            List<Book> books = person.get().getBooks();
            if (!books.isEmpty()){
                for (Book book : books){
                    long days = ChronoUnit.DAYS.between(book.getDate_issue(), LocalDateTime.now());
                    book.setOverdue(days >= 10);
                }
            }
            return person.get();
        }
        else
            return null;
    }
    @Transactional
    public void addBook(int id ,int bookId){
        Person person = findById(id);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()){
            person.getBooks().add(book.get());
            book.get().setPerson(person);
            book.get().setDate_issue(LocalDateTime.now());
        }
    }

    @Transactional
    public void deleteById(int id){
        peopleRepository.deleteById(id);
    }
    @Transactional
    public void updateById(int id, Person person){
        Person personToBeUpdated = findById(id);
        personToBeUpdated.setBooks(person.getBooks());
        personToBeUpdated.setBirthYear(person.getBirthYear());
        personToBeUpdated.setFullName(person.getFullName());
    }

    public Person findByUsername(String username){
        Optional<Person> person = peopleRepository.findByUsername(username);
        return person.orElse(null);
    }

}
