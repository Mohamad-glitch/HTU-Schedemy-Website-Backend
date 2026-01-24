package com.yazan.bank.service;

import com.yazan.bank.dto.request.CreateInstructorsRequest;
import com.yazan.bank.model.Department;
import com.yazan.bank.model.Instructors;
import com.yazan.bank.repository.InstructorsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorsServiceTest {

    @Mock
    private InstructorsRepository instructorsRepository;

    @InjectMocks
    private InstructorsService instructorsService;

    private CreateInstructorsRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateInstructorsRequest();
        request.setName("Dr. Yazan");
        request.setDepartment(Department.COMPUTER_SCIENCE);
        request.setEmail("yazan@htu.edu.jo");
        request.setJobTitle("Professor");
    }

    @Test
    void addInstructor_Success() {
        when(instructorsRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(instructorsRepository.save(any(Instructors.class))).thenAnswer(i -> i.getArguments()[0]);

        Instructors result = instructorsService.addInstructor(request);

        assertNotNull(result);
        assertEquals("Dr. Yazan", result.getName());
        verify(instructorsRepository, times(1)).save(any());
    }

    @Test
    void addInstructor_ConflictException() {
        when(instructorsRepository.findByName("Dr. Yazan")).thenReturn(Optional.of(new Instructors()));

        assertThrows(ResponseStatusException.class, () -> instructorsService.addInstructor(request));
    }

    @Test
    void getInstructorsByDepartmentOrdered_LogicCheck() {
        // Ensuring primary department comes first
        Department primary = Department.COMPUTER_SCIENCE;
        Instructors primaryDoc = new Instructors();
        primaryDoc.setName("Primary");

        when(instructorsRepository.findByDepartmentAndJobTitleNot(primary, "TA")).thenReturn(List.of(primaryDoc));

        List<Instructors> result = instructorsService.getInstructorsByDepartmentOrdered(primary);

        assertFalse(result.isEmpty());
        assertEquals("Primary", result.get(0).getName());
    }
}