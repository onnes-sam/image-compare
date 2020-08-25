# Image Compare

API for comparing two images. Uses opencv and springboot to compare two images.

# Getting Started
* Run the image-compare.jar using command:
  java -jar image-compare/target/compare-0.0.1-SNAPSHOT.jar
* Launch http://localhost:8080/
* Upload csv file and click "Import CSV" button
* The images in the csv will be compared
* The results can be downloaded in the link from the results page

##Technology Stack:
* Java 11
* Maven
* Spring Boot

##Tool Prerequisites:
* [JDK11](https://sdkman.io/)
For MacOS, JDK11 can be installed by [following MacOS instructions](https://installvirtual.com/install-openjdk-11-mac-using-brew/)
For Windows10, JDK11 can be installed by [following Windows instructions](http://techoral.com/blog/java/adoptopenjdk-install-windows.html)
*  [IntelliJ IDE Community Edition](https://www.jetbrains.com/idea/)
[MacOS](https://www.jetbrains.com/idea/download/download-thanks.html?platform=mac&code=IIC)
[Windows](https://www.jetbrains.com/idea/download/download-thanks.html?platform=windows&code=IIC)

# Importing to IntelliJ
Perform the following Steps:
* `File` > `New` > `Project from Existing Sources...`
* Select the root folder of this project
* Select `Import project from external model`
* Highlight `Maven` and click `Finish`
* Add JDK 11 in the project structure
    Please refer: [Adding JDK11 to project structure](https://www.jetbrains.com/help/idea/sdk.html#configure-external-documentation)

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
