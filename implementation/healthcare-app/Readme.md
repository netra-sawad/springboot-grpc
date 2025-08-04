


##gRPC UI
```bash
grpcui -plaintext localhost:9090
```

## Register Patient
```bash
  grpcurl -plaintext -d '{
  "first_name": "Ram",
  "last_name": "Bahadur",
  "email": "ram@gmail.com",
  "phone": "123-456-7890",
  "address": "123 Main St KTM"
}' localhost:9090 com.ns.fwu.patient.PatientService/RegisterPatient
```

## Get Patient
```bash
grpcurl -plaintext -d '{
 "patient_id": "1"
}' localhost:9090 com.ns.fwu.patient.PatientService/GetPatientDetails
```
## Register Doctor
```bash
  grpcurl -plaintext -d '{
  "first_name": "Harry",
  "last_name": "Thomas",
  "email": "harry@gmail.com",
  "phone": "123-456-7890",
  "specialty": "Cardiology",
  "centre_name": "Health Centre",
  "location": "Kathmandu"
}' localhost:9091 com.ns.fwu.doctor.DoctorService/RegisterDoctor
```

## Book Appointment 
```bash
grpcurl -plaintext -d '{
  "doctor_id": 1,
  "patient_id": 1,
  "appointment_date": "2025-02-15",
  "appointment_time": "10:00",
  "reason": "Routine check-up"
}' localhost:9092 com.ns.fwu.appointment.AppointmentService/BookAppointment
```