package com.hospital.management.service.implementation;

import com.hospital.management.model.Salon;
import com.hospital.management.repository.SalonRepository;
import com.hospital.management.service.util.SalonServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalonServiceImpl implements SalonServiceUtil {

    @Autowired
    SalonRepository salonRepository;

    @Override
    public List<Salon> findAll() {
        return salonRepository.findAll();
    }
}
