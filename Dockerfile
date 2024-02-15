FROM gcr.io/distroless/java21-debian12:nonroot
ENV TZ="Europe/Oslo"
EXPOSE 8000
COPY target/familie-ef-soknad-api.jar /app/app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
CMD ["-jar", "/app/app.jar"]
