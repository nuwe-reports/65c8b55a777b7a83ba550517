package com.example.demo.entities;

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
        
        if (timeOverlaps(appointment)){
            if (isSameRoom(appointment)){ 
                return true;
            }

            if (isSameDoctorOrPatient(appointment)) {
                return true;
            }

        }
        return false;
    }

    private boolean isSameRoom(Appointment appointment) {
        return appointment.getRoom().getRoomName().equals(this.getRoom().getRoomName());
    }

    public boolean timeOverlaps(Appointment appointment) {
        // Check if the appointments overlap in time
        LocalDateTime start1 = this.getStartsAt();
        LocalDateTime end1 = this.getFinishesAt();
        
        LocalDateTime start2 = appointment.getStartsAt();
        LocalDateTime end2 = appointment.getFinishesAt();

        // Full overlap
        boolean isFullOverlap = (start1.compareTo(start2) < 0 && end1.compareTo(end2) > 0) ||
                        (start2.compareTo(start1) < 0 && end2.compareTo(end1) > 0);

        boolean isPartialOverlap = start1.compareTo(end2) < 0 && start2.compareTo(end1) < 0;


        return isFullOverlap || isPartialOverlap;

    }

    public boolean isSameDoctorOrPatient(Appointment appointment) {
        // Check if the same doctor or patient has another appointment at the same time
        return  (this.doctor != null && appointment.doctor != null && 
                this.doctor.getId() == appointment.doctor.getId()) || 
                (this.patient != null && appointment.patient != null && 
                this.patient.getId() == appointment.patient.getId());
    }

}
