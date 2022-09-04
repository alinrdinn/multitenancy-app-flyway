package com.ali.multitenant.service.services;

import java.util.List;

import com.ali.multitenant.service.domain.entity.Employee;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(String email);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee, String email);

    void deleteEmployeeById(String email);
}
