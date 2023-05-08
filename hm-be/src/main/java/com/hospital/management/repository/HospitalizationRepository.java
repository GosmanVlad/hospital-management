package com.hospital.management.repository;

import com.hospital.management.model.Department;
import com.hospital.management.model.Employee;
import com.hospital.management.model.Hospitalization;
import com.hospital.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HospitalizationRepository extends JpaRepository<Hospitalization, Long>, JpaSpecificationExecutor<Hospitalization> {
}
