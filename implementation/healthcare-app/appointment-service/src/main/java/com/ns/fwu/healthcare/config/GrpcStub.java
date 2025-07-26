package com.ns.fwu.healthcare.config;

import com.ns.fwu.doctor.DoctorServiceGrpc;
import com.ns.fwu.patient.PatientServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcStub {


    @Bean
    DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return DoctorServiceGrpc.newBlockingStub(channelFactory.createChannel("doctorService"));
    }

    @Bean
    PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return PatientServiceGrpc.newBlockingStub(channelFactory.createChannel("patientService"));
    }

}
