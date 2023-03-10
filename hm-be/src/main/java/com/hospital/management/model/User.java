package com.hospital.management.model;

import javax.persistence.*;

import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String firstName;
    private String lastName;

    private Date createdDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;
    private String password;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

}
