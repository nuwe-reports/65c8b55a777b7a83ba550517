package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

	private Doctor d1;

	private Patient p1;

    private Room r1;
    private Room r2;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;
    private Appointment a4;

    /** TODO
     * Implement tests for each Entity class: Doctor, Patient, Room and Appointment.
     * Make sure you are as exhaustive as possible. Coverage is checked ;)
     */
    /** Rom Tests */
    @BeforeEach
	void setUp() {
		// Initialize test data before each test method
		d1 = new Doctor("John", "Doe", 35, "john.doe@example.com");
		p1 = new Patient("Alice", "Smith", 30, "alice.smith@example.com");
		r1 = new Room("Room A");
        r2 = new Room("Room B");

		// Additional setup for appointments if needed
        a1 = new Appointment(p1, d1, r1,
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
                LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));
            
        a2 = new Appointment(p1, d1, r1,
            LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
            LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));

        a3 = new Appointment(p1, d1, r1,
            LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0),
            LocalDateTime.of(2024, Month.FEBRUARY, 11, 12, 0));
        
        a4 = new Appointment(p1, d1, r2,
            LocalDateTime.of(2024, Month.FEBRUARY, 11, 10, 0),
            LocalDateTime.of(2024, Month.FEBRUARY, 11, 11, 0));

        // Persist entities in the database
        entityManager.persistAndFlush(d1);
        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(r1);
        entityManager.persistAndFlush(a1);
        entityManager.persistAndFlush(a2);
        entityManager.persistAndFlush(a3);

	}

	@Test
	void testRoomEntity() {
		// Test Room entity
		entityManager.persistAndFlush(r1);
		Room foundRoom = entityManager.find(Room.class, r1.getRoomName());
		assertNotNull(foundRoom);
		assertEquals(r1, foundRoom);
	}

    @Test
	void testDoctorEntity() {
		// Test Doctor entity
		entityManager.persistAndFlush(d1);
		Doctor foundDoctor = entityManager.find(Doctor.class, d1.getId());
		assertNotNull(foundDoctor);
		assertEquals(d1, foundDoctor);
	}

    @Test
	void testPatientEntity() {
		// Test Patient entity
		entityManager.persistAndFlush(p1);
		Patient foundPatient = entityManager.find(Patient.class, p1.getId());
		assertNotNull(foundPatient);
		assertEquals(p1, foundPatient);
	}

    @Test
    void testAppointmentEntity() {
        // Retrieve the persisted appointment from the database
        Appointment foundAppointment = entityManager.find(Appointment.class, a1.getId());

        // Verify that the appointment is not null and its attributes are correct
        assertNotNull(foundAppointment);
        assertEquals(a1.getDoctor(), foundAppointment.getDoctor());
        assertEquals(a1.getPatient(), foundAppointment.getPatient());
        assertEquals(a1.getRoom(), foundAppointment.getRoom());
        assertEquals(a1.getStartsAt(), foundAppointment.getStartsAt());
        assertEquals(a1.getFinishesAt(), foundAppointment.getFinishesAt());
    }

    @Test
    void testAppointmentOverlaps() {
        // Assert that the appointments overlap
        assertTrue(a1.overlaps(a2));

        // Assert that the appointments do not overlap same Room
        assertFalse(a1.overlaps(a3));

        // Assert that the appointments do not overlap different Room
        assertFalse(a1.overlaps(a4));
    }

}
