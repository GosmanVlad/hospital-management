package com.hospital.management.repository;

import com.hospital.management.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalonRepository extends JpaRepository<Salon, Long>, JpaSpecificationExecutor<Salon> {
    @Query(value = "SELECT * FROM salons WHERE salon_id = :salonId", nativeQuery = true)
    Salon findBySalonId(@Param("salonId") Long salonId);
}
