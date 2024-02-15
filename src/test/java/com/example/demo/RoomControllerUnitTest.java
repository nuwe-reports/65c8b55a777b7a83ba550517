package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Room;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    void testGetAllRooms_ReturnsNoContentWhenEmpty() throws Exception {
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