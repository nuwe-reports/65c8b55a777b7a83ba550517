package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="doctors")
public class Doctor extends Person {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
 
    public Doctor() {
        super();
    }
    public Doctor(String firstName, String lastName, int age, String email){
        super(firstName, lastName, age, email);
    }

   public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    
}
