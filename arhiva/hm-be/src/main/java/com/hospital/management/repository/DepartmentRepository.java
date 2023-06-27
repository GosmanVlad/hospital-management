package com.hospital.management.repository;

import com.hospital.management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    @Query(value = "SELECT * FROM departments WHERE department_id = :departmentId", nativeQuery = true)
    Department findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query(value = "SELECT * FROM departments WHERE department_name = :departmentName", nativeQuery = true)
    Department findByDepartmentName(@Param("departmentName") String departmentName);
}
