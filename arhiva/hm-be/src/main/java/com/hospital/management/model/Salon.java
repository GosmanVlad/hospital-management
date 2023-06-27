package com.hospital.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "salons")
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "salon_id",nullable = false)
    private Long salonId;

    private Date date;
    private Long seats;
    private String salonName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "salon")
    @JsonIgnore
    private Collection<Hospitalization> hospitalizations;

}
