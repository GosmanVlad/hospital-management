package com.hospital.management.repository;

import com.hospital.management.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE appointments SET status = :status WHERE appointment_id = :appointmentId", nativeQuery = true)
    void changeAppointmentStatus(@Param("appointmentId") Long appointmentId, @Param("status") String status);

    @Query(value = "SELECT * FROM appointments WHERE date = :date", nativeQuery = true)
    Appointment findByDate(@Param("date") LocalDateTime date);

    @Query(value = "SELECT * FROM appointments WHERE employee_id = :doctorId", nativeQuery = true)
    List<Appointment> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query(value = "SELECT * FROM appointments WHERE user_id = :patientId", nativeQuery = true)
    List<Appointment> findByPatientId(@Param("patientId") Long patientId);
}
