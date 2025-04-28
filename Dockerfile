FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/hamburg-academy-website-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
EXPOSE 8182