package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

    /** Rom Tests */
    @BeforeEach
	void setUp() {
		// Initialize test data before each test method
		

	}
    

    @Test
    void testDoctorEntity() {
        // Create and persist a Doctor
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        entityManager.persistAndFlush(doctor);

        // Retrieve the Doctor from the database and assert its fields
        Doctor retrievedDoctor = entityManager.find(Doctor.class, doctor.getId());
        assertThat(retrievedDoctor).isEqualTo(doctor);
        assertThat(retrievedDoctor.getFirstName()).isEqualTo("John");
        assertThat(retrievedDoctor.getLastName()).isEqualTo("Doe");
        assertThat(retrievedDoctor.getAge()).isEqualTo(35);
        assertThat(retrievedDoctor.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
	void testPatientEntity() {

        Patient patient = new Patient("John", "Doe", 35, "john.doe@example.com");
        entityManager.persistAndFlush(patient);

        // Retrieve the Patient from the database and assert its fields
        Patient retrievedPatient = entityManager.find(Patient.class, patient.getId());
        assertThat(retrievedPatient).isEqualTo(patient);
        assertThat(retrievedPatient.getFirstName()).isEqualTo("John");
        assertThat(retrievedPatient.getLastName()).isEqualTo("Doe");
        assertThat(retrievedPatient.getAge()).isEqualTo(35);
        assertThat(retrievedPatient.getEmail()).isEqualTo("john.doe@example.com");
		// Test Patient entity
		
	}

    @Test
    void testRoomEntity() {
        // Create and persist a Room
        Room room = new Room("Room 101");
        entityManager.persistAndFlush(room);

        // Retrieve the Room from the database and assert its fields
        Room retrievedRoom = entityManager.find(Room.class, room.getRoomName());
        assertThat(retrievedRoom).isEqualTo(room);
        assertThat(retrievedRoom.getRoomName()).isEqualTo("Room 101");
        
    }


    @Test
    void testAppointmentEntity() {
        // Create and persist a Doctor, a Patient, and a Room
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        Patient patient = new Patient("Jane", "Doe", 35, "john.doe@example.com");
        Room room = new Room("Room 101");
        entityManager.persistAndFlush(doctor);
        entityManager.persistAndFlush(patient);
        entityManager.persistAndFlush(room);

        // Create and persist an Appointment
        LocalDateTime appointmentTime = LocalDateTime.now().plusDays(1);
        Appointment appointment =  new Appointment(patient, doctor, room,
        appointmentTime,
        appointmentTime.plusHours(1));

        entityManager.persistAndFlush(appointment);

        // Retrieve the Appointment from the database and assert its fields
        Appointment retrievedAppointment = entityManager.find(Appointment.class, appointment.getId());
        assertThat(retrievedAppointment).isEqualTo(appointment);
        assertThat(retrievedAppointment.getStartsAt()).isEqualTo(appointmentTime);
        assertThat(retrievedAppointment.getFinishesAt()).isEqualTo(appointmentTime.plusHours(1));
        assertThat(retrievedAppointment.getDoctor()).isEqualTo(doctor);
        assertThat(retrievedAppointment.getPatient()).isEqualTo(patient);
        assertThat(retrievedAppointment.getRoom()).isEqualTo(room);
    }

    
    @Test
    void testNoOverlappingAppointments() {
        // Create appointments with different time intervals and rooms

        Doctor d1 = new Doctor("John", "Doe", 35, "john.doe@example.com");
		Patient p1 = new Patient("Alice", "Smith", 30, "alice.smith@example.com");
		Room r1 = new Room("Room A");
        
        Doctor d2 = new Doctor("Jane", "Doe", 35, "jane.doe@example.com");
        Patient p2 = new Patient("janice", "Smith", 30, "alice.smith@example.com");
        Room r2 = new Room("Room B");

        Appointment a1 = new Appointment(p1, d1, r1,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
        Appointment a2 = new Appointment(p2, d2, r2,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 12, 0));
    
        // Assert that the appointments do not overlap
        assertFalse(a1.overlaps(a2));
    }
    
    @Test
    void testOverlappingInRoom() {
        // Create appointments with the same time interval but different rooms
        Room r = new Room("Room A");
                
        Doctor d1 = new Doctor("John", "Doe", 35, "john.doe@example.com");
		Patient p1 = new Patient("Alice", "Smith", 30, "alice.smith@example.com");
		
        Doctor d2 = new Doctor("Jane", "Doe", 35, "jane.doe@example.com");
        Patient p2 = new Patient("janice", "Smith", 30, "alice.smith@example.com");
        
        Appointment a1 = new Appointment(p1, d1, r,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
        Appointment a2 = new Appointment(p2, d2, r,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
    
        // Assert that the appointments overlap
        assertTrue(a1.overlaps(a2));
    }
    
    @Test
    void testOverlappingInPatient() {
        // Create appointments with the same patient and overlapping time intervals
        Patient p = new Patient("Alice", "Smith", 30, "alice.smith@example.com");
        
        Doctor d1 = new Doctor("John", "Doe", 35, "john.doe@example.com");
		Room r1 = new Room("Room A");
        
        Doctor d2 = new Doctor("Jane", "Doe", 35, "jane.doe@example.com");
        Room r2 = new Room("Room B");

        Appointment a1 = new Appointment(p, d1, r1,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
        Appointment a2 = new Appointment(p, d2, r2,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
    
        // Assert that the appointments overlap
        assertTrue(a1.overlaps(a2));
    }
    
    @Test
    void testOverlappingInDoctor() {
        // Create appointments with the same doctor and overlapping time intervals

        Doctor d = new Doctor("John", "Doe", 35, "john.doe@example.com");
		
        Patient p1 = new Patient("Alice", "Smith", 30, "alice.smith@example.com");
		Room r1 = new Room("Room A");
        
        Patient p2 = new Patient("janice", "Smith", 30, "alice.smith@example.com");
        Room r2 = new Room("Room B");
        
        Appointment a1 = new Appointment(p1, d, r1,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
        Appointment a2 = new Appointment(p2, d, r2,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
    
        // Assert that the appointments overlap
        assertTrue(a1.overlaps(a2));
    }
    
    @Test
    void testTimeOverlaps_FullOverlap() {
        Room r = new Room("Room 101");

        Appointment appointment1 = new Appointment();
        appointment1.setRoom(r);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 12, 0));
        
        Appointment appointment2 = new Appointment();
        appointment2.setRoom(r);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 13, 0));

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    void testTimeOverlaps_FullOverlap2() {
        Room r = new Room("Room 101");

        Appointment appointment1 = new Appointment();
        appointment1.setRoom(r);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 14, 0));
        
        Appointment appointment2 = new Appointment();
        appointment2.setRoom(r);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 13, 0));

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    void testTimeOverlaps_FullOverlap3() {
        Room r = new Room("Room 101");

        Appointment appointment1 = new Appointment();
        appointment1.setRoom(r);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 11, 30));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 12, 30));
        
        Appointment appointment2 = new Appointment();
        appointment2.setRoom(r);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 13, 0));

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    void testTimeOverlaps_PartialOverlap() {
        Appointment appointment1 = new Appointment();
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 9, 0)); // Partially overlaps with appointment2
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));

        Appointment appointment2 = new Appointment();
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0)); // Partially overlaps with appointment1
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 12, 0));

        assertTrue(appointment1.timeOverlaps(appointment2));
    }


    @Test
    void testTimeOverlaps_PartialOverlap2() {
        Appointment appointment1 = new Appointment();
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 9, 0)); // Partially overlaps with appointment2
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));

        Appointment appointment2 = new Appointment();
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 8, 0)); // Partially overlaps with appointment1
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 10, 0));

        assertTrue(appointment1.timeOverlaps(appointment2));
    }

    @Test
    void testTimeOverlaps_NonPartialOverlap() {
        Appointment appointment1 = new Appointment();
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 17, 0)); // Partially overlaps with appointment2
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 17, 30));

        Appointment appointment2 = new Appointment();
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 8, 0)); // Partially overlaps with appointment1
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 10, 0));

        assertFalse(appointment1.timeOverlaps(appointment2));
    }


    @Test
    void testIsSameDoctorOrPatient_SameDoctor() {
        Appointment appointment1 = new Appointment();
        Doctor doctor = new Doctor();
        doctor.setId(1); // Set doctor id
        appointment1.setDoctor(doctor);
        
        Appointment appointment2 = new Appointment();
        appointment2.setDoctor(doctor);

        assertTrue(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_SamePatient() {
        Appointment appointment1 = new Appointment();
        Patient patient = new Patient();
        patient.setId(1); // Set doctor id
        appointment1.setPatient(patient);
        
        Appointment appointment2 = new Appointment();
        appointment2.setPatient(patient);

        assertTrue(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_DifferentDoctorSamePatient() {
        Appointment appointment1 = new Appointment();
        Doctor doctor1 = new Doctor();
        doctor1.setId(1);
        appointment1.setDoctor(doctor1);

        Appointment appointment2 = new Appointment();
        Doctor doctor2 = new Doctor();
        doctor2.setId(2); // Different doctor id
        appointment2.setDoctor(doctor2);
        appointment2.setPatient(appointment1.getPatient()); // Same patient as appointment1

        assertFalse(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_DifferentDoctorDifferentPatient() {
        Appointment appointment1 = new Appointment();
        Doctor doctor1 = new Doctor();
        doctor1.setId(1);
        appointment1.setDoctor(doctor1);
        Patient patient1 = new Patient();
        patient1.setId(1);
        appointment1.setPatient(patient1);

        Appointment appointment2 = new Appointment();
        Doctor doctor2 = new Doctor();
        doctor2.setId(2); // Different doctor id
        appointment2.setDoctor(doctor2);
        Patient patient2 = new Patient();
        patient2.setId(2); // Different patient id
        appointment2.setPatient(patient2);

        assertFalse(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_NullDoctorOrPatient() {
        Appointment appointment1 = new Appointment();
        Doctor doctor = new Doctor();
        doctor.setId(1);
        appointment1.setDoctor(doctor);

        Appointment appointment2 = new Appointment();
        // Null doctor and patient

        assertFalse(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_DifferentPatientSameDoctor() {
        Appointment appointment1 = new Appointment();
        Doctor doctor = new Doctor();
        doctor.setId(1);
        appointment1.setDoctor(doctor);

        Patient patient1 = new Patient();
        patient1.setId(1);
        appointment1.setPatient(patient1);

        Appointment appointment2 = new Appointment();
        Patient patient2 = new Patient();
        patient2.setId(2); // Different patient id
        appointment2.setPatient(patient2);
        appointment2.setDoctor(appointment1.getDoctor()); // Same doctor as appointment1

        assertTrue(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_NullDoctorAndPatient() {
        Appointment appointment1 = new Appointment();
        // Null doctor and patient

        Appointment appointment2 = new Appointment();
        // Null doctor and patient

        assertFalse(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_NullDoctorDifferentPatient() {
        Appointment appointment1 = new Appointment();
        Patient patient1 = new Patient();
        patient1.setId(1);
        appointment1.setPatient(patient1);

        Appointment appointment2 = new Appointment();
        // Null doctor but different patient

        assertFalse(appointment1.isSameDoctorOrPatient(appointment2));
    }

    @Test
    void testIsSameDoctorOrPatient_NullPatientDifferentDoctor() {
        Appointment appointment1 = new Appointment();
        Doctor doctor1 = new Doctor();
        doctor1.setId(1);
        appointment1.setDoctor(doctor1);

        Appointment appointment2 = new Appointment();
        // Null patient but different doctor

        assertFalse(appointment1.isSameDoctorOrPatient(appointment2));
    }


}
