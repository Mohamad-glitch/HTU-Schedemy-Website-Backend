package com.yazan.bank.service;

import com.yazan.bank.model.Rooms;
import com.yazan.bank.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void addRoom_ThrowsConflict_IfRoomExists() {
        when(roomRepository.findByName("S-207")).thenReturn(Optional.of(new Rooms("S-207")));

        assertThrows(ResponseStatusException.class, () -> roomService.addRoom("S-207"));
    }

    @Test
    void addRoom_Success() {
        when(roomRepository.findByName("NewRoom")).thenReturn(Optional.empty());
        when(roomRepository.save(any(Rooms.class))).thenAnswer(i -> i.getArguments()[0]);

        Rooms saved = roomService.addRoom("NewRoom");
        assertEquals("NewRoom", saved.getName());
    }
}