package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.Doctor;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class DoctorEntityUnitTest {

    @Test
    void testConstructor() {
        Doctor doctor = new Doctor();
        assertNotNull(doctor);
    }

    @Test
    void testParameterizedConstructor() {
        Doctor doctor = new Doctor("John", "Doe", 30, "john.doe@example.com");
        assertNotNull(doctor);
        assertEquals("John", doctor.getFirstName());
        assertEquals("Doe", doctor.getLastName());
        assertEquals(30, doctor.getAge());
        assertEquals("john.doe@example.com", doctor.getEmail());
    }
    @Test
    void testGettersAndSetters() {
        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setAge(30);
        doctor.setEmail("john.doe@example.com");

        assertEquals(1, doctor.getId());
        assertEquals("John", doctor.getFirstName());
        assertEquals("Doe", doctor.getLastName());
        assertEquals(30, doctor.getAge());
        assertEquals("john.doe@example.com", doctor.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        Doctor doctor1 = new Doctor("John", "Doe", 30, "john.doe@example.com");
        Doctor doctor2 = doctor1;
        Doctor doctor3 = new Doctor("Jane", "Smith", 25, "jane.smith@example.com");

        assertEquals(doctor1, doctor2); // Test equality
        assertNotEquals(doctor1, doctor3); // Test inequality
        assertEquals(doctor1.hashCode(), doctor2.hashCode()); // Test hashCode consistency
    }
   


}
