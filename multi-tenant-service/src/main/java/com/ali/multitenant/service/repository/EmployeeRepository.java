package com.ali.multitenant.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ali.multitenant.service.domain.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}