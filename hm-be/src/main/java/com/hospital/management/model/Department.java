package com.hospital.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id",nullable = false)
    private Long departmentId;
    private String departmentName;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Collection<Employee> employees;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Collection<Appointment> appointments;
}
