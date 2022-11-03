FROM ghcr.io/navikt/baseimages/temurin:17
ENV APPLICATION_NAME=familie-ef-soknad-api
EXPOSE 8000
COPY ./target/familie-ef-soknad-api-1.0-SNAPSHOT.jar "app.jar"
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
