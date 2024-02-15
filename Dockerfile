FROM gcr.io/distroless/java21-debian12:nonroot
ENV TZ="Europe/Oslo"
EXPOSE 8000
COPY ./target/familie-ef-soknad-api-1.0-SNAPSHOT.jar "app.jar"
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
CMD ["-jar", "/app/app.jar"]
