package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long>{
   
}