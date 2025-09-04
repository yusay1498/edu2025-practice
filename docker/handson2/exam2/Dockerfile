FROM ubuntu

RUN apt update && \
    DEBIAN_FRONTEND=noninteractive apt upgrade -y && \
    apt install -y openjdk-21-jdk && \
    java -version

# Set a default command (optional)
CMD ["bash"]
