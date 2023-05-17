package com.hospital.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoice_id",nullable = false)
    private Long invoiceId;

    @Temporal(TemporalType.DATE)
    private Date date;

    private Long vatPercentage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private User patient;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    private Collection<InvoiceItem> invoiceItems;
}
