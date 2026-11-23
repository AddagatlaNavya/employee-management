
package com.navya.employeemanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmployeeResponse {

    @Schema(description = "Unique identifier of the employee", example = "1")
    private Long id;
    @Schema(description = "Name of the employee", example = "John Doe")
    private String name;
    @Schema(description = "Email of the employee", example = "navya@gmail.com")
    private String email;
    @Schema(description = "Department of the employee", example = "IT")
    private String department;

    public EmployeeResponse(Long id, String name,
                            String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }
}
