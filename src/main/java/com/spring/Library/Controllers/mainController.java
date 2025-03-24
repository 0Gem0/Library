package com.spring.Library.Controllers;

import com.spring.Library.Model.Person;
import com.spring.Library.Services.RegistrationService;
import com.spring.Library.Util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class mainController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;

    public mainController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors()){
            return "redirect:/auth/registration";
        }
        else{
            person.setRole("ROLE_USER");
            registrationService.register(person);
            return "redirect:/auth/login";
        }
    }
}
