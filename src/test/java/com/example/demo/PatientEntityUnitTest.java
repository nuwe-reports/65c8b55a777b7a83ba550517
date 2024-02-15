package com.example.demo;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.Patient;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class PatientEntityUnitTest {

    @Test
    void testConstructor_WithValidArguments_ShouldInitializeFields() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        int age = 30;
        String email = "john.doe@example.com";
        
        // Act
        Patient patient = new Patient(firstName, lastName, age, email);
        
        // Assert
        assertEquals(firstName, patient.getFirstName());
        assertEquals(lastName, patient.getLastName());
        assertEquals(age, patient.getAge());
        assertEquals(email, patient.getEmail());
    }

    @Test
    void testConstructor_WithNoArguments_ShouldAssignDefaultValues() {
        // Act
        Patient patient = new Patient();
        
        // Assert
        assertNull(patient.getFirstName());
        assertNull(patient.getLastName());
        assertEquals(0, patient.getAge());
        assertNull(patient.getEmail());
    }


	@Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Patient patient = new Patient();
        String firstName = "Jane";
        String lastName = "Smith";
        int age = 25;
        String email = "jane.smith@example.com";
        
        // Act
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setEmail(email);
        
        // Assert
        assertEquals(firstName, patient.getFirstName());
        assertEquals(lastName, patient.getLastName());
        assertEquals(age, patient.getAge());
        assertEquals(email, patient.getEmail());
    }

    @Test
    void testEquals_ShouldReturnTrue_WhenPatientsAreEqual() {
        // Arrange
        Patient patient1 = new Patient("John", "Doe", 30, "john.doe@example.com");
        Patient patient2 = patient1;
        
        // Assert
        assertTrue(patient1.equals(patient2));
    }

    @Test
    void testEquals_ShouldReturnFalse_WhenPatientsAreNotEqual() {
        // Arrange
        Patient patient1 = new Patient("John", "Doe", 30, "john.doe@example.com");
        Patient patient2 = new Patient("Jane", "Smith", 25, "jane.smith@example.com");
        
        // Assert
        assertFalse(patient1.equals(patient2));
    }

    @Test
    void testHashCode_ShouldReturnSameValue_WhenPatientsAreEqual() {
        // Arrange
        Patient patient1 = new Patient("John", "Doe", 30, "john.doe@example.com");
        Patient patient2 = patient1;
        
        // Assert
        assertEquals(patient1.hashCode(), patient2.hashCode());
    }

    @Test
    void testConstructor_WithNegativeAge_ShouldAssignZeroAge() {
        // Act
        Patient patient = new Patient("John", "Doe", -5, "john.doe@example.com");
        
        // Assert
        assertEquals(0, patient.getAge());
    }

    
    @Test
    void testConstructor_WithNullArguments_ShouldAssignNullValues() {
        // Act
        Patient patient = new Patient(null, null, 0, null);
        
        // Assert
        assertNull(patient.getFirstName());
        assertNull(patient.getLastName());
        assertEquals(0, patient.getAge());
        assertNull(patient.getEmail());
    }

    @Test
    void testSetters_WithNullArguments_ShouldNotThrowException() {
        // Arrange
        Patient patient = new Patient();
        
        // Act
        assertDoesNotThrow(() -> {
            patient.setFirstName(null);
            patient.setLastName(null);
            patient.setAge(0);
            patient.setEmail(null);
        });
    }

    
}
