## Installation
Setup an application-dev.properties file in src/main/resources with following sample
```console
spring.datasource.url=jdbc:postgresql://localhost:5432/healthcare_app_dbs
spring.datasource.username=postgres
spring.datasource.password=password

spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.grpc.server.port=9091
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
    1. Extend the Grpc service (for example : DoctorServiceImplBase)
    2. add data in ResponseObserver as :
        - responseObserver.onNext() : add data
        - responseObserver.onError() : Add Error.
        - responseObserver.onCompleted()  : tell Grpc execution complete.

