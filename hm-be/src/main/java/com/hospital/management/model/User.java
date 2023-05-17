package com.hospital.management.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id",nullable = false)
    private Long userId;

    private String firstName;
    private String lastName;
    private String personalNumber;
    private String phone;
    private Date createdDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private String password;
    private String role;
    private boolean activated;
    private String homeAddress;
    private String city;
    private String country;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Employee employee;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Collection<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private Collection<Invoice> invoices;

}
