package com.navya.employeemanagement.service;

import com.navya.employeemanagement.dto.request.CreateEmployeeRequest;
import com.navya.employeemanagement.dto.response.EmployeeResponse;
import com.navya.employeemanagement.entity.Employee;
import com.navya.employeemanagement.exception.DuplicateEmployeeEmailException;
import com.navya.employeemanagement.exception.EmployeeNotFoundException;
import com.navya.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(999L)
        );
    }

    @Test
    void shouldReturnEmployeeWhenEmployeeExists() {
        // Arrange
        Long employeeId = 101L;
        Employee employee = new Employee(
                "Navya",
                "navya@gmail.com",
                "IT"
        );
        ReflectionTestUtils.setField(employee, "id", employeeId);
        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        // Act
        EmployeeResponse response = employeeService.getEmployeeById(employeeId);

        // Assert
        assertNotNull(response);
        assertEquals(employeeId, response.getId());
        assertEquals("Navya", response.getName());
        assertEquals("navya@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Rahul");
        request.setEmail("rahul@gmail.com");
        request.setDepartment("IT");

        when(employeeRepository.existsByEmail("rahul@gmail.com"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                DuplicateEmployeeEmailException.class,
                () -> employeeService.createEmployee(request)
        );

        // Verify
        verify(employeeRepository, never())
                .save(any(Employee.class));
    }
    @Test
    void shouldCreateEmployeeSuccessfully() {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Navya");
        request.setEmail("navya@gmail.com");
        request.setDepartment("IT");

        Employee savedEmployee = new Employee(
                "Navya",
                "navya@gmail.com",
                "IT"
        );

        when(employeeRepository.existsByEmail("navya@gmail.com"))
                .thenReturn(false);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);
        ReflectionTestUtils.setField(savedEmployee, "id", 101L);

        // Act
        EmployeeResponse response = employeeService.createEmployee(request);

        // Assert
        assertNotNull(response);
        assertEquals(101L, response.getId());
        assertEquals("Navya", response.getName());
        assertEquals("navya@gmail.com", response.getEmail());
        assertEquals("IT", response.getDepartment());

        // Verify
        verify(employeeRepository, times(1))
                .save(any(Employee.class));
    }
}
