package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_METHOD)
class AppointmentEntityUnitTest {
   @Test
    void testConstructor() {
        Patient patient = new Patient("John", "Doe", 30, "john.doe@example.com");
        Doctor doctor = new Doctor("Jane", "Smith", 25, "jane.smith@example.com");
        Room room = new Room("Room A");
        LocalDateTime startsAt = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt = LocalDateTime.of(2022, 1, 1, 11, 0);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        assertNotNull(appointment);
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(room, appointment.getRoom());
        assertEquals(startsAt, appointment.getStartsAt());
        assertEquals(finishesAt, appointment.getFinishesAt());
    }

    @Test
    void testGettersAndSetters() {
        Appointment appointment = new Appointment();
        Patient patient = new Patient("John", "Doe", 30, "john.doe@example.com");
        Doctor doctor = new Doctor("Jane", "Smith", 25, "jane.smith@example.com");
        Room room = new Room("Room A");
        LocalDateTime startsAt = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt = LocalDateTime.of(2022, 1, 1, 11, 0);

        appointment.setId(1);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setRoom(room);
        appointment.setStartsAt(startsAt);
        appointment.setFinishesAt(finishesAt);

        assertEquals(1, appointment.getId());
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(room, appointment.getRoom());
        assertEquals(startsAt, appointment.getStartsAt());
        assertEquals(finishesAt, appointment.getFinishesAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Patient patient = new Patient("John", "Doe", 30, "john.doe@example.com");
        Doctor doctor = new Doctor("Jane", "Smith", 25, "jane.smith@example.com");
        Room room = new Room("Room A");
        LocalDateTime startsAt = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt = LocalDateTime.of(2022, 1, 1, 11, 0);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt, finishesAt);
        Appointment appointment2 = appointment1;
        Appointment appointment3 = new Appointment(patient, doctor, room, startsAt.plusHours(1), finishesAt.plusHours(1));

        assertEquals(appointment1, appointment2); // Test equality
        assertNotEquals(appointment1, appointment3); // Test inequality
        assertEquals(appointment1.hashCode(), appointment2.hashCode()); // Test hashCode consistency
    }

   @Test
    void testOverlap() {
        Patient patient1 = new Patient("John", "Doe", 30, "john.doe@example.com");
        Doctor doctor1 = new Doctor("Jane", "Smith", 25, "jane.smith@example.com");
        Room room1 = new Room("Room A");
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);
        
        Patient patient2 = new Patient("Alice", "Johnson", 35, "alice.johnson@example.com");
        Doctor doctor2 = new Doctor("Bob", "Brown", 28, "bob.brown@example.com");
        Room room2 = new Room("Room A");
        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 10, 30); // Overlaps with appointment 1
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 11, 30);

        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2)); // Both appointments overlap
        assertTrue(appointment2.overlaps(appointment1)); // Overlapping relationship is symmetric
    }


}
