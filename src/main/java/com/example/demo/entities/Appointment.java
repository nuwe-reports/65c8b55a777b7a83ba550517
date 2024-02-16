package com.example.demo.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "roomName")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Room room;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime startsAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime finishesAt;

    public Appointment(){
        super();
    }

    public Appointment( Patient patient, Doctor doctor, Room room, LocalDateTime startsAt, LocalDateTime finishesAt){
        this.patient = patient;
        this.doctor = doctor;
        this.room = room;
        this.startsAt =  startsAt;
        this.finishesAt =  finishesAt;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }
    
    public LocalDateTime getStartsAt(){
        return this.startsAt;
    }
    public void setStartsAt(LocalDateTime startsAt){
        this.startsAt = startsAt;
    }
    
    public LocalDateTime getFinishesAt(){
        return this.finishesAt;
    }
    public void setFinishesAt(LocalDateTime finishesAt){
        this.finishesAt = finishesAt;
    }

    public Patient getPatient(){
        return this.patient;
    }
    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public Doctor getDoctor(){
        return this.doctor;
    }
    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public Room getRoom(){
        return this.room;
    }
    public void setRoom(Room room){
        this.room = room;
    }
    
    public boolean overlaps( Appointment appointment){
        /// True when:
        // Case 1: A.starts == B.starts
        // Case 2: A.finishes == B.finishes 
        // Case 3: A.starts < B.finishes && B.finishes < A.finishes
        // Case 4: B.starts < A.starts && A.finishes < B.finishes
        if (appointment.getRoom().getRoomName().equals(this.getRoom().getRoomName())){ 
            if (this.getStartsAt().equals(appointment.getStartsAt()) || 
                    appointment.getFinishesAt().equals(this.getFinishesAt())){
                return true;
                    }
            if (appointment.getFinishesAt().isAfter(this.getStartsAt()) && appointment.getFinishesAt().isBefore(this.getFinishesAt())){
                return true;
            }
            if ( appointment.getStartsAt().isAfter(this.getStartsAt()) && appointment.getStartsAt().isBefore(this.getFinishesAt())){
                return true;
            }
        }
        
        return false;
    }

}
