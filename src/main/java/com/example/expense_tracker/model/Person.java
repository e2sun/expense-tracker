package com.example.expense_tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.util.HashSet;          
import java.util.Set;            
import jakarta.persistence.ManyToMany;


@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "participants")
    private Set<Expense> expensesParticipatingIn = new HashSet<>();

    public Person(){

    }

    public Person(String name){
        this.name = name;
    }

    // getters and setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Set<Expense> getExpensesParticipatingIn(){
        return expensesParticipatingIn;
    }

}