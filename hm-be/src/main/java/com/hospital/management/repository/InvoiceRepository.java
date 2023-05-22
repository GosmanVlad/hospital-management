package com.hospital.management.repository;

import com.hospital.management.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    @Query(value = "SELECT * FROM invoices WHERE invoice_id = :invoiceId", nativeQuery = true)
    Invoice findByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE invoices SET status = :status WHERE invoice_id = :invoiceId", nativeQuery = true)
    void updateStatus(@Param("invoiceId") Long invoiceId, @Param("status") String status);

    @Query(value = "SELECT * FROM invoices WHERE doctor_id = :doctorId AND date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Invoice> findByDateBetweenAndDoctorId(@Param("doctorId") Long doctorId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT * FROM invoices WHERE doctor_id = :doctorId", nativeQuery = true)
    List<Invoice> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query(value = "SELECT * FROM invoices WHERE patient_id = :patientId AND status = 'unpaid'", nativeQuery = true)
    List<Invoice> findByPatientIdAndStatusUnpaid(@Param("patientId") Long patientId);

    @Query(value = "SELECT * FROM invoices WHERE patient_id = :patientId AND date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Invoice>  findByDateBetweenAndPatientId(@Param("patientId") Long patientId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
