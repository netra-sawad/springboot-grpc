package com.ns.fwu.healthcare.service;


import com.ns.fwu.healthcare.entity.Patient;
import com.ns.fwu.healthcare.repositoty.PatientRepository;
import com.ns.fwu.patient.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientRepository patientRepository;

    @Override
    public void getPatientDetails(PatientDetailsRequest request, StreamObserver<PatientDetails> responseObserver) {
        var patient = patientRepository.findById(request.getPatientId());
        if (patient.isPresent()) {
            var p = patient.get();
            responseObserver.onNext(PatientDetails.newBuilder()
                            .setPatientId(p.getId())
                            .setFirstName(p.getFirstName())
                            .setLastName(p.getLastName())
                            .setEmail(p.getEmail())
                            .setAddress(p.getAddress())
                            .setPhone(p.getPhone())
                    .build());
        } else{
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("Patient not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void registerPatient(PatientRegistrationRequest request, StreamObserver<PatientRegistrationResponse> responseObserver) {
        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        patientRepository.save(patient);

        responseObserver.onNext(PatientRegistrationResponse.newBuilder().setPatientId(patient.getId())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<PatientRegistrationRequest> streamPatient(StreamObserver<RegistrationMessage> responseObserver) {
        return new StreamObserver<PatientRegistrationRequest>() {
            @Override
            public void onNext(PatientRegistrationRequest patientRegistrationRequest) {
                Patient patient = Patient.builder()
                        .firstName(patientRegistrationRequest.getFirstName())
                        .lastName(patientRegistrationRequest.getLastName())
                        .email(patientRegistrationRequest.getEmail())
                        .phone(patientRegistrationRequest.getPhone())
                        .address(patientRegistrationRequest.getAddress())
                        .build();
                patientRepository.save(patient);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(io.grpc.Status.INTERNAL.withDescription(throwable.getMessage()).asRuntimeException());

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(RegistrationMessage.newBuilder().setMessage("Patient Register successfully.").build());
                responseObserver.onCompleted();

            }
        };
    }
}
