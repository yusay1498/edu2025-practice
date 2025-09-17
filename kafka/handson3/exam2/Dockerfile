FROM amazoncorretto:17.0.7

EXPOSE 8080

ARG JAR

COPY $JAR /opt/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]