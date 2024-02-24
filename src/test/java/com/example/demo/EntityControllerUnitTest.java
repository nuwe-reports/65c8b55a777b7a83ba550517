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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


class EntityControllerUnitTest{

    @Nested
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

    @Nested
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
        void testGetAllPatients_ReturnsPatientsWhenNotEmpty() throws Exception {
            // Create some sample patients
            Patient patient1 = new Patient("John", "Doe", 30, "john@example.com");
            Patient patient2 = new Patient("Alice", "Smith", 25, "alice@example.com");
            List<Patient> patients = Arrays.asList(patient1, patient2);

            // Mock the patient repository to return the sample patients
            when(patientRepository.findAll()).thenReturn(patients);

            // Perform the GET request to retrieve all patients
            mockMvc.perform(get("/api/patients"))
                    .andExpect(status().isOk()) // Expect HTTP 200 OK
                    .andExpect(jsonPath("$", hasSize(2))) // Expecting 2 patients in the response
                    .andExpect(jsonPath("$[0].firstName").value(patient1.getFirstName()))
                    .andExpect(jsonPath("$[0].lastName").value(patient1.getLastName()))
                    .andExpect(jsonPath("$[0].age").value((patient1.getAge())))
                    .andExpect(jsonPath("$[0].email").value(patient1.getEmail()))
                    
                    .andExpect(jsonPath("$[1].firstName").value(patient2.getFirstName()))
                    .andExpect(jsonPath("$[1].lastName").value(patient2.getLastName()))
                    .andExpect(jsonPath("$[1].age").value((patient2.getAge())))
                    .andExpect(jsonPath("$[1].email").value(patient2.getEmail()));
                    
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

    @Nested
    @WebMvcTest(RoomController.class)
    class RoomControllerUnitTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RoomRepository roomRepository;

        @Test
        void testGetAllRooms_ReturnsRoomsWhenNonEmpty() throws Exception {
            // Create some sample rooms
            List<Room> rooms = new ArrayList<>();
            rooms.add(new Room("Room 1"));
            rooms.add(new Room("Room 2"));
            rooms.add(new Room("Room 3"));

            // Mock the roomRepository to return the sample rooms
            when(roomRepository.findAll()).thenReturn(rooms);

            // Perform the GET request to retrieve all rooms
            mockMvc.perform(get("/api/rooms")
                    .contentType(MediaType.APPLICATION_JSON))
                    // Expect status OK because rooms exist
                    .andExpect(status().isOk())
                    // Optionally, you can also validate the response body to ensure correct rooms are returned
                    .andExpect(jsonPath("$.length()").value(3))
                    .andExpect(jsonPath("$[0].roomName").value("Room 1"))
                    .andExpect(jsonPath("$[1].roomName").value("Room 2"))
                    .andExpect(jsonPath("$[2].roomName").value("Room 3"));
        }


        @Test
        void testGetAllRooms_ReturnsNoContentWhenNonEmpty() throws Exception {
            when(roomRepository.findAll()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/api/rooms")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        void testGetRoomByRoomName_ReturnsRoomWhenExists() throws Exception {
            String roomName = "Room 101";
            Room room = new Room(roomName);
            when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.of(room));

            mockMvc.perform(get("/api/rooms/{roomName}", roomName)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roomName").value(roomName));
        }

        @Test
        void testGetRoomByRoomName_ReturnsNotFoundWhenNotExists() throws Exception {
            String roomName = "Room 101";
            when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.empty());

            mockMvc.perform(get("/api/rooms/{roomName}", roomName)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testCreateRoom_ReturnsCreatedWithRoomDetails() throws Exception {
            String roomName = "New Room";
            Room room = new Room(roomName);
            when(roomRepository.save(any(Room.class))).thenReturn(room);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(room);

            mockMvc.perform(post("/api/room")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.roomName").value(roomName));
        }

        @Test
        void testDeleteRoom_ReturnsOkWhenExists() throws Exception {
            String roomName = "Room 101";
            Room room = new Room(roomName);
            when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.of(room));

            mockMvc.perform(delete("/api/rooms/{roomName}", roomName)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void testDeleteRoom_ReturnsNotFoundWhenNotExists() throws Exception {
            String roomName = "Nonexistent Room";
            when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.empty());

            mockMvc.perform(delete("/api/rooms/{roomName}", roomName)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteAllRooms_ReturnsOk() throws Exception {
            mockMvc.perform(delete("/api/rooms")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }
}