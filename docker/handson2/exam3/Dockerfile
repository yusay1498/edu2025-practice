FROM ubuntu

RUN apt update && \
    DEBIAN_FRONTEND=noninteractive apt upgrade -y && \
    apt install -y openjdk-21-jdk && \
    java -version

COPY ./dummy-api/target/dummy-api-0.0.1-SNAPSHOT.jar /opt/demo.jar

WORKDIR /opt

# Set a default command (optional)
ENTRYPOINT ["java", "-jar", "demo.jar"]