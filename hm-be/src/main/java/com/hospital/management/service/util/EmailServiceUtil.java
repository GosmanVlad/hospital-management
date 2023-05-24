package com.hospital.management.service.util;

import com.hospital.management.model.dto.email.EmailData;

public interface EmailServiceUtil {
    void sendMail(EmailData details);
}
