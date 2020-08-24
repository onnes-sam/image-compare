#Image Compare

API for comparing two images. Uses opencv and springboot to compare two images.

#Getting Started

Launch http://localhost:8080/
Upload csv and click "Import CSV"

##Technology Stack:
* Java 11
* Maven
* Spring Boot

##Tool Prerequisites:
* JDK 11 (https://sdkman.io/)
* IntelliJ IDE (https://www.jetbrains.com/idea/)

# Importing to IntelliJ
Perform the following Steps:
* `File` > `New` > `Project from Existing Sources...`
* Select the root folder of this project
* Select `Import project from external model`
* Highlight `Maven` and click `Finish`

# Build
To build:
```
./mvnw clean install
```
## Run from IntelliJ
* Right click CompareApplication -> Run/Debug

## Run from Maven
```
 mvn spring-boot:run -pl image-compare
```

## Build 'fat jar' and run
```
mvn clean install
java -jar image-compare/target/image-compare.jar