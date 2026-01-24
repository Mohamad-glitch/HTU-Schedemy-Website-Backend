package com.yazan.bank.service;

import com.yazan.bank.dto.request.CreateScheduleRequest;
import com.yazan.bank.model.*;
import com.yazan.bank.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulesServiceTest {

    @Mock private SchedulesRepository scheduleRepository;
    @Mock private InstructorsRepository instructorsRepository;
    @Mock private CoursesRepository coursesRepository;
    @Mock private RoomRepository roomsRepository;
    @Mock private TimeSlotRepository timeSlotsRepository;

    @InjectMocks
    private SchedulesService schedulesService;

    @Test
    void addSchedulesById_ShouldDecreaseLoadForNewInstructor() {
        // 1. Mock Request
        CreateScheduleRequest request = new CreateScheduleRequest();
        request.setInstructorId(10L);
        request.setRoomId(1L);
        request.setTimeSlotId(1L);

        // 2. Mock Entities
        Instructors instructor = new Instructors();
        instructor.setId(10L);
        Rooms room = new Rooms("S-207");
        TimeSlots slot = new TimeSlots();
        Schedules existingSchedule = new Schedules(slot, room); // Old instructor is null

        // 3. Mock Repository Behaviors
        when(instructorsRepository.findById(10L)).thenReturn(Optional.of(instructor));
        when(roomsRepository.findById(1L)).thenReturn(Optional.of(room));
        when(timeSlotsRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(scheduleRepository.findByRoomAndTimeslot(room, slot)).thenReturn(Optional.of(existingSchedule));
        when(scheduleRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        // 4. Execute
        List<Schedules> result = schedulesService.addSchedulesById(List.of(request));

        // 5. Verify Teaching Load Logic
        assertNotNull(result);
        verify(instructorsRepository, times(1)).decreaseUnassignedTeachingLoad(10L);
        verify(instructorsRepository, never()).increaseUnassignedTeachingLoad(anyLong());
    }

    @Test
    void deleteScheduleById_ThrowsExceptionIfNotFound() {
        when(scheduleRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> schedulesService.deleteScheduleById(1L));
    }
}