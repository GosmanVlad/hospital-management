package com.hospital.management.repository;

import com.hospital.management.model.Hospitalization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface HospitalizationRepository extends JpaRepository<Hospitalization, Long>, JpaSpecificationExecutor<Hospitalization> {
    @Query(value = "SELECT * FROM hospitalization WHERE salon_id = :salonId AND end_date > :date", nativeQuery = true)
    List<Hospitalization> findBySalonIdAndDate(@Param("salonId") Long salonId, @Param("date") Date date);

    @Query(value = "SELECT * FROM hospitalization WHERE hospitalization_id = :hospitalizationId", nativeQuery = true)
    Hospitalization findByHospitalizationId(@Param("hospitalizationId") Long hospitalizationId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE hospitalization SET doc_path = :docPath WHERE hospitalization_id = :hospitalizationId", nativeQuery = true)
    void updateHospitalizationDocPath(@Param("hospitalizationId") Long hospitalizationId, @Param("docPath") String path);
}
