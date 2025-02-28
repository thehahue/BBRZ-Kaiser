FROM openjdk:23
LABEL authors="BBRZ-Java"

COPY target/kaiser*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]