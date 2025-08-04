## Installation
Setup an application-dev.properties file in src/main/resources with following sample
```console
spring.application.name=appointment-service
spring.datasource.url= jdbc:postgresql://localhost:5432/healthcare_app_dbs
spring.datasource.username=postgres
spring.datasource.password=password

spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update

spring.grpc.server.port=9092

spring.grpc.client.channels.doctorService.address=0.0.0.0:9091
spring.grpc.client.channels.patientService.address=0.0.0.0:9090
```
<div class="termy">

Using mvn package
```console
$ mvn install
```
```console
$ mvn install
```
</div>

- Make the generated source as Marked as Generated source 
### Call Another service
    1. Make configuration with channel name 
    2. Extend the Grpc service (for example : AppointmentServiceImplBase)
    3. Use the BlockingStub service(eg:DoctorServiceBlockingStub) use its methos for blocking call.

