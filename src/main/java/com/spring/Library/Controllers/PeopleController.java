package com.spring.Library.Controllers;

import com.spring.Library.Model.Book;
import com.spring.Library.Model.Person;
import com.spring.Library.Services.BookService;
import com.spring.Library.Services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private PeopleService peopleService;

    private BookService bookService;


    @Autowired
    public PeopleController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping()
    public String getAllPeoples(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "adminRole/people/people";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model,@ModelAttribute("book") Book book){
          Person person = peopleService.findById(id);
          model.addAttribute("person",person);
        List<Book> books = bookService.findAvailableBooks();
        model.addAttribute("books",books);
//        Person person = personDao.findById(id);
//        person.setBooks(new ArrayList<>());
//        List<Book> books = bookDao.showAll();
//        for (Book book : books) {
//            if (book.getPersonId() != null && book.getPersonId().equals(id)){
//                person.getBooks().add(book);
//            }
//        }
//        model.addAttribute("person",person);
        return "adminRole/people/person";
    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "/adminRole/people/newPerson";
    }

    @PostMapping()
    public String create(@ModelAttribute @Valid Person person, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "adminRole/people/newPerson";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

//    @GetMapping("{/id}")
//    public String addBook(@PathVariable("id") int id,Model model ,@ModelAttribute("book") Book book){
//        Person person = peopleService.findById(id);
//        model.addAttribute("person", person);
//        List<Book> books = bookService.findAvailableBooks();
//        model.addAttribute("books",books);
//        return "Person";
//    }

    @GetMapping("/edit/{id}")
    public String edit(@ModelAttribute("person") Person person){
        return "/adminRole/people/editPerson";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "adminRole/people/editPerson";
        }
        peopleService.updateById(id,person);

        return "redirect:/people/{id}";
    }

    @PostMapping("/{id}")
    public String addBook(@PathVariable("id") int id, @RequestParam("bookId") int bookId, Model model){
        peopleService.addBook(id,bookId);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id , @ModelAttribute("person") Person person){
        peopleService.deleteById(id);
        return "redirect:/people";
    }
}
