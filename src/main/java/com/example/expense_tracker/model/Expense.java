package com.example.expense_tracker.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToMany; 
import jakarta.persistence.JoinTable; 
import java.util.HashSet;              
import java.util.Set; 
import java.math.BigDecimal;


@Entity 
@Table(name = "expenses")
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paid_by_person_id")
    private Person paidBy;

    @ManyToMany
    @JoinTable(
        name = "expense_participants",
        joinColumns = @JoinColumn(name = "expense_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> participants = new HashSet<>();

    public Expense(){

    }

    public Expense(String description, BigDecimal amount, Person paidBy){
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

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public Person getPaidBy(){
        return paidBy;
    }

    public void setPaidBy(Person paidBy){
        this.paidBy = paidBy;
    }

    public Set<Person> getParticipants() { 
        return participants;
    }

    public void setParticipants(Set<Person> participants) { 
        this.participants = participants;
    }

}
