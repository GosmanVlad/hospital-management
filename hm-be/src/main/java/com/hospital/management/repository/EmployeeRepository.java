package com.hospital.management.repository;

import com.hospital.management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Query(value = "SELECT * FROM employees WHERE department_id = :departmentId", nativeQuery = true)
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query(value = "SELECT * FROM employees WHERE employee_id = :employeeId", nativeQuery = true)
    Employee findByEmployeeId(@Param("employeeId") Long employeeId);

    @Query(value = "SELECT * FROM employees WHERE user_id = :userId", nativeQuery = true)
    Employee findByUserId(@Param("userId") Long userId);
}
