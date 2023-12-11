# Use the official OpenJDK image as the base image
FROM openjdk:22-slim-bullseye

# Set the working directory to /app
WORKDIR /app

ARG DEPS="curl unzip zip make"

# Install Kotlin
RUN apt-get update && \
    apt-get install -y ${DEPS} && \
    curl -s https://get.sdkman.io | bash && \
    bash -c "source /root/.sdkman/bin/sdkman-init.sh && sdk install kotlin" && \
    apt-get clean

# Set up the entry point to an interactive shell
ENV TARGET=""
ENV OUTPUT=""
CMD ["/bin/bash", "-cl", "kotlinc -include-runtime -d $OUTPUT $TARGET"]
