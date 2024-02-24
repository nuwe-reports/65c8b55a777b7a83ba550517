package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
    void testOverlapsWhenSameStartAndFinish() {
        Room room = new Room("Room 101");
        
        // Arrange
        Appointment appointment1 = new Appointment();
        appointment1.setRoom(room);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));

        Appointment appointment2 = new Appointment();
        appointment2.setRoom(room);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        // Act
        boolean result = appointment1.overlaps(appointment2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testOverlapsWhenSameRoomAndStartDifferentFinish() {
        Room room = new Room("Room 101");
        // Arrange
        
        Appointment appointment1 = new Appointment();
        appointment1.setRoom(room);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));

        Appointment appointment2 = new Appointment();
        appointment2.setRoom(room);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 30));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        // Act
        boolean result = appointment1.overlaps(appointment2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testOverlapsWhenSameRoomAndFinishDifferentStart() {
        Room room = new Room("Room 101");
        
        Appointment appointment1 = new Appointment();
        appointment1.setRoom(room);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        
        Appointment appointment2 = new Appointment();
        appointment2.setRoom(room);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 9, 0));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 10, 30));
        // Act
        boolean result = appointment1.overlaps(appointment2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testOverlapsWhenDifferentRoom() {
        // Arrange
        Room room1 = new Room("Room 101");
        Appointment appointment1 = new Appointment();
        appointment1.setRoom(room1);
        appointment1.setStartsAt(LocalDateTime.of(2022, 1, 1, 10, 0));
        appointment1.setFinishesAt(LocalDateTime.of(2022, 1, 1, 11, 0));
        
        Room room2 = new Room("Room 102");
        Appointment appointment2 = new Appointment();
        appointment2.setRoom(room2);
        appointment2.setStartsAt(LocalDateTime.of(2022, 1, 1, 9, 0));
        appointment2.setFinishesAt(LocalDateTime.of(2022, 1, 1, 10, 30));

        

        // Act
        boolean result = appointment1.overlaps(appointment2);

        // Assert
        assertFalse(result);
    }


}
