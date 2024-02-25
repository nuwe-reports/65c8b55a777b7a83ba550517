package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    // Constructor with all repositories for constructor injection
    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id){
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()){
            return new ResponseEntity<>(appointment.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean isInvalidAppointment(Appointment appointment){
        return appointment.getDoctor() == null || 
            appointment.getPatient() == null || appointment.getRoom() == null || 
            appointment.getStartsAt() == null || appointment.getFinishesAt() == null;
    }

    private boolean isOverlappingAppointment(Appointment appointment) {
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        return existingAppointments.stream()
                .anyMatch(existing -> existing.overlaps(appointment));
    }

    private boolean isTimeIntervalValid(Appointment appointment) {
        return appointment.getStartsAt().compareTo(appointment.getFinishesAt()) >= 0;
    }

    @PostMapping("/appointment")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        
        if (isInvalidAppointment(appointment)) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        Doctor doc = appointment.getDoctor();
        Patient pat = appointment.getPatient();

        Room room = appointment.getRoom();
        
        LocalDateTime startsAt = appointment.getStartsAt();
        LocalDateTime finishesAt = appointment.getFinishesAt();
        
        
        if (isTimeIntervalValid(appointment)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        if( isOverlappingAppointment(appointment))
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    
        Appointment tmp = new Appointment(pat, doc, room, startsAt, finishesAt);
        
        appointmentRepository.save(tmp);

        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id){

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments(){
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
