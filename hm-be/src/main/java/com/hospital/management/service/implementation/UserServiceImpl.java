package com.hospital.management.service.implementation;

import com.hospital.management.model.*;
import com.hospital.management.model.dto.department.DepartmentCsvLayout;
import com.hospital.management.model.dto.department.DepartmentImportRequest;
import com.hospital.management.model.dto.people.PeopleCsvLayout;
import com.hospital.management.model.dto.people.PeopleRequestImport;
import com.hospital.management.model.dto.user.UserOutcomingDto;
import com.hospital.management.model.dto.user.UserUpdateRequest;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.EmployeeRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.service.util.FileServiceUtil;
import com.hospital.management.service.util.UserServiceUtil;
import com.hospital.management.utils.ResponseUtils;
import com.opencsv.bean.CsvToBeanBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserServiceUtil {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileServiceUtil fileUploadService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<User> findAllPatients() {
        return userRepository.findAllPatients();
    }

    @Override
    public Page<User> findAllPatientsWithPagination(Long userId, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(User_.role), "PATIENT"));

            if(userId != null) {
                predicates.add(criteriaBuilder.equal(root.get(User_.userId), userId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public void importFromCsv(PeopleRequestImport importDTO) throws Exception {
        try {
            String csvPath = fileUploadService.createDirectoriesAndSaveFile(importDTO.getDoc());
            File file = new File(csvPath);

            List<PeopleCsvLayout> result = readFile(file);
            for (PeopleCsvLayout row : result) {
                try {
                    User user = new User();

                    user.setFirstName(row.getFirstName());
                    user.setLastName(row.getLastName());
                    user.setEmail(row.getEmail());
                    user.setPhone(row.getPhone());
                    user.setBirthdate(row.getBirthDate());
                    user.setCountry(row.getCountry());
                    user.setCity(row.getCity());
                    user.setHomeAddress(row.getHomeAddress());
                    user.setPersonalNumber(row.getPersonalNumber());
                    user.setRole(row.getRole());
                    user.setCreatedDate(new Date());
                    user.setActivated(false);
                    user.setUsername(row.getFirstName() + " " + row.getLastName());
                    user.setPassword(new BCryptPasswordEncoder().encode("gsmvlad"));
                    userRepository.save(user);

                    if (!row.getRole().equals("PATIENT")) {
                        Employee employee = new Employee();
                        Department department = departmentRepository.findByDepartmentName(row.getDepartmentName());

                        employee.setUser(user);
                        employee.setDepartment(department);
                        employeeRepository.save(employee);
                    }
                }catch(Exception exception) {
                    exception.printStackTrace();
                }
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public UserOutcomingDto getUserProfile(Long userId) {
        User user = userRepository.findByUserId(userId);
        UserOutcomingDto userOutcomingDto = new UserOutcomingDto();

        if(user != null) {
            userOutcomingDto.setCreatedDate(user.getCreatedDate());
            userOutcomingDto.setEmail(user.getEmail());
            userOutcomingDto.setFirstName(user.getFirstName());
            userOutcomingDto.setLastName(user.getLastName());
            userOutcomingDto.setBirthDate(user.getBirthdate());
            userOutcomingDto.setCity(user.getCity());
            userOutcomingDto.setCountry(user.getCountry());
            userOutcomingDto.setHomeAddress(user.getHomeAddress());
            userOutcomingDto.setPersonalNumber(user.getPersonalNumber());
            userOutcomingDto.setPhone(user.getPhone());
        }
        return userOutcomingDto;
    }

    @Override
    public void updateUserProfile(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findByUserId(userId);

        try {
            if (user != null) {
                user.setCreatedDate(userUpdateRequest.getCreatedDate());
                user.setEmail(userUpdateRequest.getEmail());
                user.setFirstName(userUpdateRequest.getFirstName());
                user.setLastName(userUpdateRequest.getLastName());
                user.setBirthdate(userUpdateRequest.getBirthDate());
                user.setCity(userUpdateRequest.getCity());
                user.setCountry(userUpdateRequest.getCountry());
                user.setHomeAddress(userUpdateRequest.getHomeAddress());
                user.setPersonalNumber(userUpdateRequest.getPersonalNumber());
                user.setPhone(userUpdateRequest.getPhone());

                userRepository.save(user);
            }
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<PeopleCsvLayout> readFile(File csvFile) throws Exception {
        List<PeopleCsvLayout> beans = new CsvToBeanBuilder(new FileReader(csvFile))
                .withType(PeopleCsvLayout.class)
                .withSeparator(',')
                //.withSkipLines(skipLine)
                .build()
                .parse();

        return beans;
    }

}
