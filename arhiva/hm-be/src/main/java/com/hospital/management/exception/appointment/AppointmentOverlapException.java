package com.hospital.management.exception.appointment;

public class AppointmentOverlapException extends Exception {
    public AppointmentOverlapException(String errorMessage){
        super(errorMessage);
    }
}
