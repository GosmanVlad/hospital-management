package com.hospital.management.service.util;

import com.hospital.management.model.User;

public interface UserServiceUtil {
    User findByEmail(String email);

    void save(User user);

    User findByUsername(String email);

    User findByUserId(Long userId);
}
