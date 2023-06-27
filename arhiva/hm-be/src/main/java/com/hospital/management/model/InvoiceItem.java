package com.hospital.management.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoice_item_id",nullable = false)
    private Long invoiceItemId;

    private String service;
    private Long brutCost;
    private Long vatPercentage;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
