package com.example.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.DoctorController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.repositories.DoctorRepository;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Test
    void testGetAllDoctors_ReturnsNoContentWhenEmpty() throws Exception {
        when(doctorRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllDoctors_ReturnsPatientsWhenNotEmpty() throws Exception {
        // Create some sample patients
        Doctor doctor1 = new Doctor("John", "Doe", 30, "john@example.com");
        Doctor doctor2 = new Doctor("Alice", "Smith", 25, "alice@example.com");
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        // Mock the patient repository to return the sample patients
        when(doctorRepository.findAll()).thenReturn(doctors);

        // Perform the GET request to retrieve all patients
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$", hasSize(2))) // Expecting 2 patients in the response
                .andExpect(jsonPath("$[0].firstName").value(doctor1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(doctor1.getLastName()))
                .andExpect(jsonPath("$[0].age").value((doctor1.getAge())))
                .andExpect(jsonPath("$[0].email").value(doctor1.getEmail()))
                
                .andExpect(jsonPath("$[1].firstName").value(doctor2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(doctor2.getLastName()))
                .andExpect(jsonPath("$[1].age").value((doctor2.getAge())))
                .andExpect(jsonPath("$[1].email").value(doctor2.getEmail()));
                
    }

    @Test
    void testGetDoctorById_ReturnsDoctorWhenExists() throws Exception {
        long id = 1;
        Doctor doctor = new Doctor("John", "Doe", 40, "john.doe@example.com");
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/doctors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(doctor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctor.getLastName()))
                .andExpect(jsonPath("$.age").value(doctor.getAge()))
                .andExpect(jsonPath("$.email").value(doctor.getEmail()));
    }

    @Test
    void testGetDoctorById_ReturnsNotFoundWhenNotExists() throws Exception {
        long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/doctors/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateDoctor_ReturnsCreatedWithDoctorDetails() throws Exception {
        Doctor doctor = new Doctor("John", "Doe", 40, "john.doe@example.com");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/api/doctor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 40, \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(doctor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctor.getLastName()))
                .andExpect(jsonPath("$.age").value(doctor.getAge()))
                .andExpect(jsonPath("$.email").value(doctor.getEmail()));
    }

    @Test
    void testDeleteDoctor_ReturnsOkWhenExists() throws Exception {
        long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.of(new Doctor("John", "Doe", 40, "john.doe@example.com")));

        mockMvc.perform(delete("/api/doctors/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteDoctor_ReturnsNotFoundWhenNotExists() throws Exception {
        long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/doctors/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAllDoctors_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }
}

