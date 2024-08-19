FROM openjdk:17

LABEL authors="Gazda Karol"

WORKDIR /app

COPY target/service2-0.0.1-SNAPSHOT.jar /app.jar

RUN ls -la /app

EXPOSE 5000

ENTRYPOINT ["java","-jar","/app.jar"]