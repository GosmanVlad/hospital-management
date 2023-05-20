package com.hospital.management.service.implementation;

import com.hospital.management.model.*;
import com.hospital.management.repository.EmployeeRepository;
import com.hospital.management.service.util.EmployeeServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeServiceUtil {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Override
    public Employee findByUserId(Long userId) {
        return employeeRepository.findByUserId(userId);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> findAllByPagination(Long employeeId, String role, Pageable pageable) {
        Specification<Employee> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (employeeId != null) {
                predicates.add(criteriaBuilder.equal(root.get(Employee_.employeeId), employeeId));
            }

            if(!role.equals("")) {
                predicates.add(criteriaBuilder.equal(root.join("user").get("role"), role));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        return employeeRepository.findAll(specification, pageable);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }
}
