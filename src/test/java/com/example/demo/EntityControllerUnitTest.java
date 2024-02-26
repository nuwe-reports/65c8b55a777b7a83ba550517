package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    private Doctor d1, d2;
    /** Doctor Tests */
    @BeforeEach
    void setUp() {
        d1 = new Doctor("John", "Doe", 30, "john.doe@example.com");
        d2 = new Doctor("Alice", "Smith", 25, "alice@example.com");
        // Initialize test data before each test method
    }

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
    void testGetAllDoctors_ReturnsDoctorsWhenNotEmpty() throws Exception {
        // Create some sample doctors
        List<Doctor> doctors = Arrays.asList(d1, d2);

        // Mock the patient repository to return the sample patients
        when(doctorRepository.findAll()).thenReturn(doctors);

        MvcResult result = mockMvc.perform(get("/api/doctors"))
            .andExpect(status().isOk())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Doctor> returnedDoctors = objectMapper.readValue(jsonResponse, new TypeReference<List<Doctor>>() {});

        // Verify the returned list has the expected size
        assertThat(returnedDoctors).hasSize(2);

        for (int i = 0; i < doctors.size(); i++) {
            Doctor expectedDoctor = doctors.get(i);
            Doctor returnedDoctor = returnedDoctors.get(i);
    
            assertThat(returnedDoctor.getFirstName()).isEqualTo(expectedDoctor.getFirstName());
            assertThat(returnedDoctor.getLastName()).isEqualTo(expectedDoctor.getLastName());
            assertThat(returnedDoctor.getAge()).isEqualTo(expectedDoctor.getAge());
            assertThat(returnedDoctor.getEmail()).isEqualTo(expectedDoctor.getEmail());
        }
    
    }

    @Test
    void testGetDoctorById_ReturnsDoctorWhenExists() throws Exception {
        long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.of(d1));

        mockMvc.perform(get("/api/doctors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(d1)));

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
        //Doctor doctor = new Doctor("John", "Doe", 40, "john.doe@example.com");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(d1);

        String doctorJson = new ObjectMapper().writeValueAsString(d1);

        mockMvc.perform(post("/api/doctor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(doctorJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(doctorJson)); // Verify response content matches the serialized Doctor object
    }

    @Test
    void testDeleteDoctor_ReturnsOkWhenExists() throws Exception {
        long id = 1;
        when(doctorRepository.findById(id)).thenReturn(Optional.of(d1));

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

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    private Patient p1, p2;
    /** Doctor Tests */
    @BeforeEach
    void setUp() {
        p1 = new Patient("John", "Doe", 30, "john.doe@example.com");
        p2 = new Patient("Alice", "Smith", 25, "alice@example.com");
        // Initialize test data before each test method
    }

    @Test
    void testGetAllPatients_ReturnsNoContentWhenEmpty() throws Exception {
        when(patientRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllPatients_ReturnsPatientsWhenNotEmpty() throws Exception {
        // Create some sample patients
        List<Patient> patients = Arrays.asList(p1, p2);

        // Mock the patient repository to return the sample patients
        when(patientRepository.findAll()).thenReturn(patients);

        MvcResult result = mockMvc.perform(get("/api/patients"))
            .andExpect(status().isOk())
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Patient> returnedPatients = objectMapper.readValue(jsonResponse, new TypeReference<List<Patient>>() {});

        // Verify the returned list has the expected size
        assertThat(returnedPatients).hasSize(2);

        for (int i = 0; i < patients.size(); i++) {
            Patient expectedPatient = patients.get(i);
            Patient returnedPatient = returnedPatients.get(i);
    
            assertThat(returnedPatient.getFirstName()).isEqualTo(expectedPatient.getFirstName());
            assertThat(returnedPatient.getLastName()).isEqualTo(expectedPatient.getLastName());
            assertThat(returnedPatient.getAge()).isEqualTo(expectedPatient.getAge());
            assertThat(returnedPatient.getEmail()).isEqualTo(expectedPatient.getEmail());
        }
                
    }


    @Test
    void testGetPatientById_ReturnsPatientWhenExists() throws Exception {
        long id = 1;
        
        when(patientRepository.findById(id)).thenReturn(Optional.of(p1));

        String patientJson = new ObjectMapper().writeValueAsString(p1);

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(patientJson)); // Verify response content matches the serialized Doctor object

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

        String patientJson = new ObjectMapper().writeValueAsString(p1);

        mockMvc.perform(post("/api/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(patientJson));
        
    }

    @Test
    void testDeletePatient_ReturnsOkWhenExists() throws Exception {
        long id = 1;
        when(patientRepository.findById(id)).thenReturn(Optional.of(p1));

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

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomRepository roomRepository;

    private Room r1, r2, r3;
    /** Room Tests */
    @BeforeEach
    void setUp() {
        // Initialize test data before each test method
        r1 = new Room("Room 1");
        r2 = new Room("Room 2");
        r3 = new Room("Room 3");
    }

    @Test
    void testGetAllRooms_ReturnsRoomsWhenNonEmpty() throws Exception {
        // Create some sample rooms
        List<Room> rooms = Arrays.asList(r1, r2, r3);;
        // Mock the roomRepository to return the sample rooms
        when(roomRepository.findAll()).thenReturn(rooms);

        MvcResult result = mockMvc.perform(get("/api/rooms"))
        .andExpect(status().isOk())
        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Room> returnedRooms = objectMapper.readValue(jsonResponse, new TypeReference<List<Room>>() {});

        // Verify the returned list has the expected size
        assertThat(returnedRooms).hasSize(3);

        for (int i = 0; i < rooms.size(); i++) {
            Room expectedRoom = rooms.get(i);
            Room returnedRoom = returnedRooms.get(i);
    
            assertThat(returnedRoom.getRoomName()).isEqualTo(expectedRoom.getRoomName());
        }

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
        String roomName = r1.getRoomName();
        when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.of(r1));

        String roomJson = new ObjectMapper().writeValueAsString(r1);

        mockMvc.perform(get("/api/rooms/{roomName}", roomName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(roomJson));
    }

    @Test
    void testGetRoomByRoomName_ReturnsNotFoundWhenNotExists() throws Exception {
        String roomName = r1.getRoomName();
        when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/rooms/{roomName}", roomName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRoom_ReturnsCreatedWithRoomDetails() throws Exception {
        
        when(roomRepository.save(any(Room.class))).thenReturn(r1);

        ObjectMapper objectMapper = new ObjectMapper();
        String roomJson = objectMapper.writeValueAsString(r1);

        mockMvc.perform(post("/api/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roomJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(roomJson));
                
    }

    @Test
    void testDeleteRoom_ReturnsOkWhenExists() throws Exception {
        String roomName = r1.getRoomName();
        when(roomRepository.findByRoomName(roomName)).thenReturn(java.util.Optional.of(r1));

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

