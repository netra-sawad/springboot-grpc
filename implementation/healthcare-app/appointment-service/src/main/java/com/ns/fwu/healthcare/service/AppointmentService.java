package com.ns.fwu.healthcare.service;

import com.ns.fwu.appointment.*;
import com.ns.fwu.doctor.DoctorDetailsRequest;
import com.ns.fwu.doctor.DoctorServiceGrpc;
import com.ns.fwu.healthcare.entity.Appointment;
import com.ns.fwu.healthcare.repository.AppointmentRepository;
import com.ns.fwu.patient.PatientDetailsRequest;
import com.ns.fwu.patient.PatientServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AppointmentService extends AppointmentServiceGrpc.AppointmentServiceImplBase {

    private final AppointmentRepository appointmentRepository;
    private final DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub;
    private final PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub, PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub) {
        this.appointmentRepository = appointmentRepository;
        this.doctorServiceBlockingStub = doctorServiceBlockingStub;
        this.patientServiceBlockingStub = patientServiceBlockingStub;
    }

    @Override
    public void bookAppointment(BookAppointmentRequest request, StreamObserver<BookAppointmentResponse> responseObserver) {
       try{
           var doctorResponse = doctorServiceBlockingStub.getDoctorDetails(DoctorDetailsRequest.newBuilder().setDoctorId(request.getDoctorId()).build());
           var patientResponse = patientServiceBlockingStub.getPatientDetails(PatientDetailsRequest.newBuilder().setPatientId(request.getPatientId()).build());

           Appointment appointment = Appointment.builder()
                   .doctorId(request.getDoctorId())
                   .patientId(request.getPatientId())
                   .doctorName(doctorResponse.getFirstName()+" "+ doctorResponse.getLastName())
                   .patientName(patientResponse.getFirstName()+" "+patientResponse.getLastName())
                   .location(doctorResponse.getLocation())
                   .appointmentDate(LocalDate.parse(request.getAppointmentDate()))
                   .appointmentTime(LocalTime.parse(request.getAppointmentTime()))
                   .reason(request.getReason())
                   .build();

           appointmentRepository.save(appointment);
           responseObserver.onNext(BookAppointmentResponse.newBuilder().setAppointmentId(appointment.getId()).build());
       } catch (Exception e) {
           responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
       }
       responseObserver.onCompleted();
    }

    @Override
    public void getAppointmentAvailable(AppointmentAvailabilityRequest request, StreamObserver<AppointmentAvailableResponse> responseObserver) {
        List<LocalDateTime> availableAppointments = Arrays.asList(
                LocalDateTime.of(2025,7,23,9,0),
                LocalDateTime.of(2025,7,24,10,0),
                LocalDateTime.of(2025,7,25,9,30),
                LocalDateTime.of(2025,7,26,11,0),
                LocalDateTime.of(2025,7,27,9,40),
                LocalDateTime.of(2025,7,28,9,20),
                LocalDateTime.of(2025,7,29,9,0),
                LocalDateTime.of(2025,7,30,9,30),
                LocalDateTime.of(2025,7,31,9,40),
                LocalDateTime.of(2025,8,1,9,40),
                LocalDateTime.of(2025,8,2,9,40)
        );

        Random random = new Random();
        int i =0;

        while (i<10){
            Collections.shuffle(availableAppointments);

            var slots = availableAppointments.stream()

                    .limit(2)
                    .map(dateTime -> AppointmentSlot.newBuilder()
                            .setAppointmentDate(dateTime.toLocalDate().toString())
                            .setAppointmentTime(dateTime.toLocalTime().toString())
                            .build())
                    .collect(Collectors.toList());

            var resposne = AppointmentAvailableResponse.newBuilder().addAllResponses(slots)
                    .setAvailabilityAsOf(LocalDateTime.now().toString()).build();
            responseObserver.onNext(resposne);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
        responseObserver.onCompleted();
    }
}
