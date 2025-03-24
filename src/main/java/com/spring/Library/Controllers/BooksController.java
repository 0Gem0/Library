package com.spring.Library.Controllers;

import com.spring.Library.Model.Book;
import com.spring.Library.Model.Person;
import com.spring.Library.Services.BookService;
import com.spring.Library.Services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {
    private PeopleService peopleService;
    private BookService bookService;

    @Autowired
    public BooksController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }


    @GetMapping()
    public String showBooks(Model model,@RequestParam(value = "sort_by_year", required = false) Boolean isSorted,@RequestParam(value = "page" , required = false) Integer page,@RequestParam(value = "books_per_page", required = false) Integer booksPerPage){
        model.addAttribute("books",bookService.findAll(isSorted, page, booksPerPage));
        System.out.println(page + "," + booksPerPage);
        return "adminRole/books/allBooks";
    }
    @GetMapping("/search")
    public String bookSearch(@ModelAttribute("book") Book book, Model model){
        model.addAttribute("isSearched", false);
        return "adminRole/books/bookSearch";
    }
    @PostMapping("/search")
    public String bookSearch(@RequestParam("name") String bookName, Model model){
        Book book = bookService.findByName(bookName);
        model.addAttribute("book", book);
        System.out.println(book.isOverdue());
        model.addAttribute("isSearched", true);
        return "adminRole/books/bookSearch";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model ,@ModelAttribute("person") Person person){
        Book book = bookService.findById(id);
        model.addAttribute("book",book);
        List<Person> people = peopleService.findAll();
        model.addAttribute("people", people);
        return "adminRole/books/oneBook";
    }

    @GetMapping("/new")
    public String createBookGet(@ModelAttribute("book") Book book){
        return "adminRole/books/bookCreate";
    }

    @GetMapping("/edit/{id}")
    public String updateBookGet(@PathVariable("id") int id, @ModelAttribute("book") Book book){
        return "adminRole/books/bookUpdate";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "adminRole/books/bookCreate";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/{bookId}")
    public String addPersonBook(@PathVariable("bookId") int bookId, @RequestParam("personId") int personId, Model model) {
        bookService.addPerson(bookId, personId);
//        book.setPerson(person);
        return "redirect:/books/{bookId}";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id , @ModelAttribute("book") Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "adminRole/books/bookUpdate";
        }
        bookService.updateById(id,book);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}/deletePerson")
    public String deletePersonBook(@PathVariable("id") int id, @ModelAttribute("book") Book book){
        book.setPerson(new Person());
        bookService.deletePerson(id);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id,@ModelAttribute("book") Book book){
        bookService.delete(id);
        return "redirect:/books";
    }


}
