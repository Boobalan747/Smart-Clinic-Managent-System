package com.clinicmanagement.service;

import com.clinicmanagement.model.Doctor;
import com.clinicmanagement.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // --------------------- CREATE ---------------------
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // --------------------- READ ALL ---------------------
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // --------------------- READ BY ID ---------------------
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));
    }

    // --------------------- UPDATE ---------------------
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor doctor = getDoctorById(id);

        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setQualification(doctorDetails.getQualification());
        doctor.setConsultationFee(doctorDetails.getConsultationFee());
        doctor.setExperience(doctorDetails.getExperience());
        doctor.setHospitalName(doctorDetails.getHospitalName());

        return doctorRepository.save(doctor);
    }

    // --------------------- DELETE ---------------------
    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }
}
