package com.hospital.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id",nullable = false)
    private Long employeeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private Collection<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Collection<Hospitalization> hospitalizations;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private Collection<Invoice> invoices;
}
