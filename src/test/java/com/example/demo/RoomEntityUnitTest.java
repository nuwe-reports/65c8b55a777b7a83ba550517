package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.Room;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_METHOD)
class RoomEntityUnitTest {
    @Test
    void testConstructor() {
        // Arrange
        String roomName = "Room A";
    
        // Act
        Room room = new Room(roomName);
    
        // Assert
        assertEquals(roomName, room.getRoomName());
    }
    
    @Test
    void testEqualsAndHashCode() {
        Room room1 = new Room("Room A");
        Room room2 = room1;
        Room room3 = new Room("Room B");

        // Test equals() method
        assertTrue(room1.equals(room2)); // Same roomName
        assertFalse(room1.equals(room3)); // Different roomName

        // Test hashCode() method
        assertEquals(room1.hashCode(), room2.hashCode()); // Hash codes should be equal for equal objects
        assertNotEquals(room1.hashCode(), room3.hashCode()); // Hash codes should be different for different objects
    }

    // Test with empty roomName
    @Test
    void testEmptyRoomName() {
        Room room = new Room("");
        assertEquals("", room.getRoomName());
    }

    
    
}
