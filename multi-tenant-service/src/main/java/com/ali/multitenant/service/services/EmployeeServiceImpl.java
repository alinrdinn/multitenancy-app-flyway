package com.ali.multitenant.service.services;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ali.multitenant.service.domain.entity.Employee;
import com.ali.multitenant.service.repository.EmployeeRepository;

@RequiredArgsConstructor
@Component
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(String email) {
        return employeeRepository.getById(email);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee, String email) {
        Employee employeeNeedToUpdate = employeeRepository.getById(email);
        employeeNeedToUpdate.setEmail(employee.getEmail());
        employeeNeedToUpdate.setPassword(employee.getPassword());
        return employeeNeedToUpdate;
    }

    @Override
    public void deleteEmployeeById(String email) {
        employeeRepository.deleteById(email);
    }
}
