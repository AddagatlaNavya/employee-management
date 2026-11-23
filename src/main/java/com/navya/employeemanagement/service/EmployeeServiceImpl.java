package com.navya.employeemanagement.service;

import com.navya.employeemanagement.dto.request.CreateEmployeeRequest;
import com.navya.employeemanagement.dto.response.EmployeeResponse;
import com.navya.employeemanagement.entity.Employee;
import com.navya.employeemanagement.exception.DuplicateEmployeeEmailException;
import com.navya.employeemanagement.exception.EmployeeNotFoundException;
import com.navya.employeemanagement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeEmailException(
                    "Employee email already exists: " + request.getEmail()
            );
        }
        Employee employee = new Employee(
                request.getName(),
                request.getEmail(),
                request.getDepartment()
        );
        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getDepartment()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee Not found with id" + id));
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(employee -> new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment()
        ));
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, CreateEmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id
                        )
                );

        if (employeeRepository.existsByEmailAndIdNot(
                request.getEmail(), id)) {

            throw new DuplicateEmployeeEmailException(
                    "Employee email already exists: " + request.getEmail()
            );
        }

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());

        Employee updatedEmployee = employeeRepository.save(employee);

        return new EmployeeResponse(
                updatedEmployee.getId(),
                updatedEmployee.getName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getDepartment()
        );
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {

        Employee employee=employeeRepository.findById(id)
                .orElseThrow(()->
                        new EmployeeNotFoundException("Employee Not found with Id: "+id));
        employeeRepository.delete(employee);
    }
}
