package com.ns.fwu.healthcare.service;


import com.ns.fwu.doctor.*;
import com.ns.fwu.healthcare.entity.Doctor;
import com.ns.fwu.healthcare.repository.DoctorRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DoctorService extends DoctorServiceGrpc.DoctorServiceImplBase {
    private final DoctorRepository doctorRepository;

    @Override
    public void getDoctorDetails(DoctorDetailsRequest request, StreamObserver<DoctorDetailsResponse> responseObserver) {
        var doctor = doctorRepository.findById(request.getDoctorId());
        if (doctor.isPresent()) {
            var d = doctor.get();
            responseObserver.onNext(DoctorDetailsResponse.newBuilder()
                            .setDoctorId(d.getId())
                            .setFirstName(d.getFirstName())
                            .setLastName(d.getLastName())
                            .setEmail(d.getEmail())
                            .setPhone(d.getPhone())
                            .setSpeciality(d.getSpeciality())
                            .setCentreName(d.getCentreName())
                            .setLocation(d.getLocation())
                    .build());
        } else {
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("Doctor with given id not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void registerDoctor(DoctorRegistrationRequest request, StreamObserver<DoctorRegistrationResponse> responseObserver) {
        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .centreName(request.getCentreName())
                .speciality(request.getSpeciality())
                .location(request.getLocation())
                .build();
        doctorRepository.save(doctor);
        responseObserver.onNext(DoctorRegistrationResponse.newBuilder().setDoctorId(doctor.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessage> responseObserver) {
        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage chatMessage) {
                String randomMessage = "Random message: " + Math.random();
                var response = ChatMessage.newBuilder().setMessage(randomMessage)
                        .setFrom("Doctor")
                        .setTo("Patient")
                        .setTimestamp(LocalTime.now().toString())
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                String finalMessage = "Chat completed";
                var response = ChatMessage.newBuilder().setMessage(finalMessage)
                        .setFrom("Doctor")
                        .setTo("Patient")
                        .setTimestamp(LocalTime.now().toString())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}
