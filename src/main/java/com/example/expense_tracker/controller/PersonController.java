package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Person;
import com.example.expense_tracker.repository.PersonRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List; 

@RestController
@RequestMapping("/api/people")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {
    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    // GET /api/people
    @GetMapping 
    public List<Person> getAllPeople(){
        return personRepository.findAll(); 
    }

    // GET /api/people/{id}
    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Long id){
        return personRepository.findById(id)
        .orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
    }

    // POST /api/people
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person){
        // validation
        if (person.getName() == null || person.getName().isBlank()) { // ✅ optional: basic validation
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        return personRepository.save(person); 
    }
}