package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient extends Person {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    public Patient(){
        super();
    }

    public Patient(String firstName, String lastName, int age, String email){
        super(firstName, lastName, Math.max(age, 0), email);
    }

    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }

}
