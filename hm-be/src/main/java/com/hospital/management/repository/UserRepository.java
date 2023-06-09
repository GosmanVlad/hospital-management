package com.hospital.management.repository;

import com.hospital.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    User findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE role = 'PATIENT'", nativeQuery = true)
    List<User> findAllPatients();

    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    User findByUserId(@Param("userId") Long userId);
}
