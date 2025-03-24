package com.spring.Library.Controllers;


import com.spring.Library.Model.Book;
import com.spring.Library.Model.Person;
import com.spring.Library.Security.PersonDetails;
import com.spring.Library.Services.BookService;
import com.spring.Library.Services.PeopleService;
import com.spring.Library.Services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
public class PersonController {

    private BookService bookService;
    private PeopleService peopleService;

    @Autowired
    public PersonController(BookService bookService, PeopleService peopleService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String showBook(@AuthenticationPrincipal PersonDetails personDetails, Model model){
        int personId = personDetails.getPerson().getId();
        Person person = peopleService.findById(personId);
        model.addAttribute("person",person);
        List<Book> books = bookService.findAvailableBooks();
        model.addAttribute("books",books);

        return "/userRole/person";
    }

    @PostMapping("/books/add")
    public String addBook(@AuthenticationPrincipal PersonDetails personDetails, @RequestParam("bookId") int bookId,Model model){
        peopleService.addBook(personDetails.getPerson().getId(),bookId);
        return "redirect:/user/books";
    }


}
