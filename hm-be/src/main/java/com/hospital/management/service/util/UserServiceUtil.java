package com.hospital.management.service.util;

import com.hospital.management.model.User;
import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.people.PeopleRequestImport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserServiceUtil {
    User findByEmail(String email);

    void save(User user);

    User findByUsername(String email);

    User findByUserId(Long userId);

    List<User> findAllPatients();

    Page<User> findAllPatientsWithPagination(Long userId, Pageable pageable);

    void importFromCsv(PeopleRequestImport importDTO) throws Exception;
}
