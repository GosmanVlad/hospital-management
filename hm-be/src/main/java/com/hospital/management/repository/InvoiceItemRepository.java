package com.hospital.management.repository;

import com.hospital.management.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long>, JpaSpecificationExecutor<InvoiceItem> {
}
