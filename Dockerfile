FROM navikt/java:11-appdynamics

ENV APPD_ENABLED=TRUE
EXPOSE 8000
COPY ./target/familie-ef-soknad-api-1.0-SNAPSHOT.jar "app.jar"
