package com.example.demo;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.AppointmentController;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(AppointmentController.class)
class AppointmentControllerUnitTest {

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testShouldCreateAppointment() throws Exception {

        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isCreated());

    }

    @Test
    void testShouldNotCreateAppointment_WithEmptyPatient() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(null, doctor, room, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testShouldNotCreateAppointment_WithEmptyDoctor() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Room room = new Room("Dermatology");

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, null, room, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testShouldNotCreateAppointment_WithEmptyRoom() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, null, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testShouldNotCreateAppointment_WithNullAppointment() throws Exception {
        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShouldNotCreateAppointment_WithNullStartsAt() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));

        // Set startsAt as null
        Appointment appointment = new Appointment(patient, doctor, room, null,
                LocalDateTime.parse("20:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));

        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testShouldNotCreateAppointment_WithNullFinishesAt() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));

        // Set finishesAt as null
        Appointment appointment = new Appointment(patient, doctor, room,
                LocalDateTime.parse("19:30 24/04/2023", DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")), null);

        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testShouldNotCreateAppointment() throws Exception {

        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void testShouldCreateOneAppointmentOutOfTwoConflictDateAndRoom() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Room room = new Room("Dermatology");

        Patient patient1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Appointment appointment1 = new Appointment(patient1, doctor1, room, startsAt, finishesAt);

        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Doctor doctor2 = new Doctor("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Appointment appointment2 = new Appointment(patient2, doctor2, room, startsAt, finishesAt);

        // Perform the first POST request to create the first appointment
        when(appointmentRepository.findAll())
            .thenReturn(Collections.emptyList())
            .thenReturn(Collections.singletonList(appointment1))
            .thenReturn(Arrays.asList(appointment1, appointment2));

        // Perform the first POST request to create the first appointment
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment1)))
            .andExpect(status().isCreated());

        // Perform the second POST request to create the second appointment
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment2)))
            .andExpect(status().isConflict());

    }

    @Test
    void testShouldCreateOneAppointmentOutOfTwoConflictDateAndDoctor() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Room room1 = new Room("Room 101");
        Room room2 = new Room("Room 102");

        Patient patient1 = new Patient("Jhon", "Doe", 37, "j.doe@email.com");
        Doctor doctor = new Doctor("Jane", "Smith", 24, "p.amalia@hospital.accwe");
        Appointment appointment1 = new Appointment(patient1, doctor, room1, startsAt, finishesAt);

        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Appointment appointment2 = new Appointment(patient2, doctor, room2, startsAt, finishesAt);

        // Perform the first POST request to create the first appointment
        when(appointmentRepository.findAll())
            .thenReturn(Collections.emptyList())
            .thenReturn(Collections.singletonList(appointment1))
            .thenReturn(Arrays.asList(appointment1, appointment2));

        // Perform the first POST request to create the first appointment
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment1)))
            .andExpect(status().isCreated());

        // Perform the second POST request to create the second appointment
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment2)))
            .andExpect(status().isConflict());

    }

    @Test
    void testShouldCreateOneAppointmentOutOfTwoConflictDateAndPatient() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);
        
        Room room1 = new Room("Room 101");
        Patient patient = new Patient("Jhon", "Doe", 37, "j.doe@email.com");
        Doctor doctor1 = new Doctor("Jane", "Smith", 24, "p.amalia@hospital.accwe");
        Appointment appointment1 = new Appointment(patient, doctor1, room1, startsAt, finishesAt);

        Room room2 = new Room("Room 102");
        Doctor doctor2 = new Doctor("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Appointment appointment2 = new Appointment(patient, doctor2, room2, startsAt, finishesAt);

        // Perform the first POST request to create the first appointment
        when(appointmentRepository.findAll())
            .thenReturn(Collections.emptyList())
            .thenReturn(Collections.singletonList(appointment1))
            .thenReturn(Arrays.asList(appointment1, appointment2));

        // Perform the first POST request to create the first appointment
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment1)))
            .andExpect(status().isCreated());

        // Perform the second POST request to create the second appointment
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment2)))
            .andExpect(status().isConflict());

    }

    @Test
    void testShouldCreateBothAppointmentsConflictDateButNotRoom() throws Exception {

        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Room room = new Room("Dermatology");
        Room room2 = new Room("Oncology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        doctor2.setId(2);
        patient2.setId(2);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment)))
            .andExpect(status().isCreated());

        List<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);

        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(post("/api/appointment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(appointment2)))
            .andExpect(status().isCreated());

    }

    @Test
    void testShouldGetNoAppointments() throws Exception {
        List<Appointment> appointments = new ArrayList<Appointment>();
        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isNoContent());

    }

    @Test
    void testShouldGetTwoAppointments() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:00 24/04/2023", formatter);
        LocalDateTime startsAt2 = LocalDateTime.parse("19:30 24/04/2023", formatter);

        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(patient2, doctor2, room, startsAt2, finishesAt2);

        List<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);
        appointments.add(appointment2);

        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk());

    }

    @Test
    void testShouldGetAppointmentById() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:00 24/04/2023", formatter);

        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        appointment.setId(1);

        Optional<Appointment> opt = Optional.of(appointment);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(appointment.getId());
        assertThat(appointment.getId()).isEqualTo(1);

        when(appointmentRepository.findById(appointment.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/appointments/" + appointment.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void testShouldNotGetAnyAppointmentById() throws Exception {
        long id = 31;
        mockMvc.perform(get("/api/appointments/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void testShouldDeleteAppointmentById() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:00 24/04/2023", formatter);

        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        appointment.setId(1);

        Optional<Appointment> opt = Optional.of(appointment);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(appointment.getId());
        assertThat(appointment.getId()).isEqualTo(1);

        when(appointmentRepository.findById(appointment.getId())).thenReturn(opt);
        mockMvc.perform(delete("/api/appointments/" + appointment.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void testShouldNotDeleteAppointment() throws Exception {
        long id = 31;
        mockMvc.perform(delete("/api/appointments/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void testShouldDeleteAllAppointments() throws Exception {
        mockMvc.perform(delete("/api/appointments"))
                .andExpect(status().isOk());

    }
}