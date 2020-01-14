FROM navikt/java:11-appdynamics
ENV APPLICATION_NAME=familie-ef-soknad-api
ENV APPD_ENABLED=TRUE
EXPOSE 8000
COPY ./sleep.sh /init-scripts/00_sleep.sh
COPY ./target/familie-ef-soknad-api-1.0-SNAPSHOT.jar "app.jar"
