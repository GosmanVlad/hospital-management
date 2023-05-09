package com.hospital.management.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "hospitalization")
public class Hospitalization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hospitalization_id",nullable = false)
    private Long hospitalizationId;

    private Date startDate;
    private Date endDate;
    private String docPath;
    private String diagnosis;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Employee doctor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salon_id")
    private Salon salon;
}
