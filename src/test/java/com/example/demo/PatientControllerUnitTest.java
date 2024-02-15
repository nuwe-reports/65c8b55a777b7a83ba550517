package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.PatientController;
import com.example.demo.entities.Patient;
import com.example.demo.repositories.PatientRepository;

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Test
    void testGetAllPatients_ReturnsNoContentWhenEmpty() throws Exception {
        when(patientRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPatientById_ReturnsPatientWhenExists() throws Exception {
        long id = 1;
        Patient patient = new Patient("John", "Doe", 30, "john@example.com");
        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("$.age").value(patient.getAge()))
                .andExpect(jsonPath("$.email").value(patient.getEmail()));
    }

    @Test
    void testGetPatientById_ReturnsNotFoundWhenNotExists() throws Exception {
        long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePatient_ReturnsCreatedWithPatientDetails() throws Exception {
        Patient patient = new Patient("John", "Doe", 30, "john@example.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 30, \"email\": \"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("$.age").value(patient.getAge()))
                .andExpect(jsonPath("$.email").value(patient.getEmail()));
    }

    @Test
    void testDeletePatient_ReturnsOkWhenExists() throws Exception {
        long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.of(new Patient("John", "Doe", 30, "john@example.com")));

        mockMvc.perform(delete("/api/patients/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePatient_ReturnsNotFoundWhenNotExists() throws Exception {
        long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/patients/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAllPatients_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }
}
