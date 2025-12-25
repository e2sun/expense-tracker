package com.example.expense_tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity 
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person paidBy;

    public Expense(){

    }

    public Expense(String description, double amount, Person paidBy){
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
    }

    // getters and setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public double getAmount(){
        return amount;
    }

    public setAmount(double amount){
        this.amount = amount;
    }

    public Person getPaidBy(){
        return paidBy;
    }

    public Person setPaidBy(Person paidBy){
        this.paidBy = paidBy;
    }

}
