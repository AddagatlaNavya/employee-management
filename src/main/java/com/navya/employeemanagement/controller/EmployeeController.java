package com.navya.employeemanagement.controller;

import com.navya.employeemanagement.dto.request.CreateEmployeeRequest;
import com.navya.employeemanagement.dto.response.EmployeeResponse;
import com.navya.employeemanagement.dto.response.PagedResponse;
import com.navya.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Tag(
        name = "Employee Management",
        description = "APIs for managing employees"
)
@RestController
@RequestMapping("/api/v1/employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Employee email already exists")
    })
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get employee by ID",
            description = "Retrieves employee details using the employee ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        EmployeeResponse response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all employees with pagination and sorting",
            description = "Retrieves a paginated list of employees with optional sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        List<String> allowedSortFields = List.of("id", "name", "department");

        if (sort.length != 2) {
            throw new IllegalArgumentException(
                    "Sort must follow the format: field,direction"
            );
        }

        String sortField = sort[0].trim();
        String sortDirection = sort[1].trim();

        if (!allowedSortFields.contains(sortField)) {
            throw new IllegalArgumentException(
                    "Sorting is not allowed for field: " + sortField
            );
        }

        if (!sortDirection.equalsIgnoreCase("asc")
                && !sortDirection.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Sort direction must be either asc or desc"
            );
        }
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.fromString(sortDirection),
                        sortField
                )
        );
        Page<EmployeeResponse> employeeResponsePage = employeeService.getAllEmployees(pageable);
        PagedResponse<EmployeeResponse> response = new PagedResponse<>(
                employeeResponsePage.getContent(),
                employeeResponsePage.getNumber(),
                employeeResponsePage.getSize(),
                employeeResponsePage.getTotalElements(),
                employeeResponsePage.getTotalPages(),
                employeeResponsePage.isFirst(),
                employeeResponsePage.isLast()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update an existing employee",
            description = "Updates the details of an existing employee using the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody CreateEmployeeRequest request) {

        EmployeeResponse response =
                employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Delete an employee",
            description = "Deletes an employee using the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
