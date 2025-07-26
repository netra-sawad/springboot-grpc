package com.ns.fwu.healthcare.repository;

import com.ns.fwu.healthcare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> , JpaSpecificationExecutor<Appointment> {
}
