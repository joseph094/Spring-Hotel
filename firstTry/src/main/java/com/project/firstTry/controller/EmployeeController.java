package com.project.firstTry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.firstTry.model.Employee;
import com.project.firstTry.repository.EmployeeRepository;

/**
 * Controller class for handling Employee-related operations.
 */
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    // Autowired EmployeeRepository for dependency injection
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Get all employees.
     *
     * @return List of Employee entities.
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        // Retrieve all employees from the repository
        return employeeRepository.findAll();
    }
}
