package com.hospital.management.repository;

import com.hospital.management.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    @Query(value = "SELECT * FROM invoices WHERE invoice_id = :invoiceId", nativeQuery = true)
    Invoice findByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE invoices SET status = :status WHERE invoice_id = :invoiceId", nativeQuery = true)
    void updateStatus(@Param("invoiceId") Long invoiceId, @Param("status") String status);
}
