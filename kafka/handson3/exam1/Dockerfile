FROM amazoncorretto:17.0.7

EXPOSE 8080

COPY ../../project/handson-kafka-purchase/target/handson-kafka-purchase-0.0.1-SNAPSHOT.jar /opt/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]