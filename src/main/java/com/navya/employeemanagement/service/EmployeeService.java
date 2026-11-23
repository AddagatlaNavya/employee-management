
package com.navya.employeemanagement.service;

import com.navya.employeemanagement.dto.request.CreateEmployeeRequest;
import com.navya.employeemanagement.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    EmployeeResponse updateEmployee(
            Long id, CreateEmployeeRequest request);

    void deleteEmployee(Long id);
}
