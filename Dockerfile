#This openJDK image could be used for local Spring Boot Applications
FROM eclipse-temurin:17 AS builder
#Changes the current working directory to “workspace”
WORKDIR workspace
#Builds argument specifying the location of the application JAR file in your project
ARG JAR_FILE=build/libs/*.jar
#Copies the application JAR file from the local machine into the image
COPY ${JAR_FILE} catalog-service.jar
#Extracts the layers from the archive applying the layered-JAR mode
RUN java -Djarmode=layertools -jar catalog-service.jar extract

FROM eclipse-temurin:17
##Security is really important to run the entrypoint process. Root user should not be able to access the docker host.
#Creates a “spring” user
RUN useradd spring
#Configures “spring” as the current user
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
#Uses the Spring Boot Launcher to start the application from the layers rather than an uber-JAR
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]