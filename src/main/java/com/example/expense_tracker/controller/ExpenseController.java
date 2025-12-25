package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Expense;
import com.example.expense_tracker.model.Person;
import com.example.expense_tracker.repository.ExpenseRepository;
import com.example.expense_tracker.repository.PersonRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal; 
import java.util.HashSet; 
import java.util.List; 

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:4200")
public class ExpenseController {
    private final ExpenseRepository expenseRepository;
    private final PersonRepository personRepository;

    public ExpenseController(ExpenseRepository expenseRepository, PersonRepository personRepository){
        this.expenseRepository = expenseRepository;
        this.personRepository = personRepository;
    }

    public record CreateExpenseRequest(
        String description,
        BigDecimal amount,
        Long paidByPersonId,
        List<Long> participantPersonIds
    ){}

    // GET /api/expenses
    @GetMapping
    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }

    // GET /api/expenses/{id}
    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable Long id){
        return expenseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
    }

    // POST /api/expenses
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense createExpense(@RequestBody CreateExpenseRequest req){
        // validation
        if (req.amount() == null || req.amount().compareTo(BigDecimal.ZERO) <= 0) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be > 0");
        }
        if (req.paidByPersonId() == null) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "paidByPersonId is required");
        }
        if (req.participantPersonIds() == null || req.participantPersonIds().isEmpty()) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "participantPersonIds is required");
        }

        Person payer = personRepository.findById(req.paidByPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paid-by person not found"));

        List<Person> participantPeople = personRepository.findAllById(req.participantPersonIds()); 
        if (participantPeople.size() != req.participantPersonIds().size()) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more participant IDs are invalid");
        }

        Expense expense = new Expense();
        expense.setDescription(req.description());
        expense.setAmount(req.amount());
        expense.setPaidBy(payer);
        expense.setParticipants(new HashSet<>(participantPeople));

        return expenseRepository.save(expense);
    }

    // PUT /api/expenses/{id}
    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody CreateExpenseRequest req){
        Expense existing = expenseRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        if (req.amount() == null || req.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be > 0");
        }

        if (req.paidByPersonId() == null) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "paidByPersonId is required");
        }

        if (req.participantPersonIds() == null || req.participantPersonIds().isEmpty()) { 
           
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "participantPersonIds is required");
        }
        Person payer = personRepository.findById(req.paidByPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paid-by person not found"));

        List<Person> participantPeople = personRepository.findAllById(req.participantPersonIds());
        if (participantPeople.size() != req.participantPersonIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more participant IDs are invalid");
        }

        existing.setDescription(req.description());
        existing.setAmount(req.amount());
        existing.setPaidBy(payer);
        existing.setParticipants(new HashSet<>(participantPeople));

        return expenseRepository.save(existing);
    }

    // DELETE /api/expenses/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id){
        if (!expenseRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found");
        }
        expenseRepository.deleteById(id);
    }

}
