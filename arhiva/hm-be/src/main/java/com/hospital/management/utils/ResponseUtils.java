package com.hospital.management.utils;

import com.hospital.management.model.Appointment;
import com.hospital.management.model.dto.appointment.AppointmentOutcomingDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseUtils {
    public static Map<String, Object> createResponseMap(Boolean error, String message, Object result) {
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("error", error);
        responseMap.put("message", message);

        if (result != null) {
            responseMap.put("result", result);
        }

        return responseMap;
    }

    public static List<?> mapEntityListToDtoList(ModelMapper modelMapper, Page<?> entityList, Class<?> dtoClassType) {
        return entityList.getContent().stream()
                .map(object -> modelMapper.map(object, dtoClassType))
                .collect(Collectors.toList());
    }
}
