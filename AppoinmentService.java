package com.clinicmanagement.service;

import com.clinicmanagement.model.Appointment;
import com.clinicmanagement.model.AppointmentStatus;
import com.clinicmanagement.repository.AppointmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // --------------------- BOOK APPOINTMENT ---------------------
    public Appointment bookAppointment(Appointment appointment) {
        // Check for overlapping appointments for the same doctor
        boolean conflict = appointmentRepository.existsByDoctorIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                appointment.getDoctor().getId(),
                appointment.getEndTime(),
                appointment.getStartTime()
        );

        if (conflict) {
            throw new IllegalStateException("Appointment slot is already booked for this doctor.");
        }

        appointment.setStatus(AppointmentStatus.BOOKED);
        return appointmentRepository.save(appointment);
    }

    // --------------------- UPDATE APPOINTMENT STATUS ---------------------
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    // --------------------- GET APPOINTMENT BY ID ---------------------
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + id));
    }

    // --------------------- LIST ALL APPOINTMENTS ---------------------
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // --------------------- LIST APPOINTMENTS FOR DOCTOR ---------------------
    public List<Appointment> getAppointmentsByDoctor(Long doctorId, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.findByDoctorIdAndStartTimeBetween(doctorId, from, to);
    }

    // --------------------- LIST APPOINTMENTS FOR PATIENT ---------------------
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // --------------------- CANCEL APPOINTMENT ---------------------
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }
}
