package com.clinicmanagement.repository;

import com.clinicmanagement.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find patient by email
    Patient findByEmail(String email);

    // Find patient by phone number
    Patient findByPhone(String phone);

    // Search patients by name (partial match)
    List<Patient> findByFullNameContainingIgnoreCase(String name);

    // Optional: find patients linked to a specific user account
    Patient findByUserId(Long userId);
}
