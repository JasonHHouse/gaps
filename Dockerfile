FROM openjdk:11.0.3-jre-slim

EXPOSE 32400

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY target/Gaps-0.0.4.jar /usr/src/app/

ENTRYPOINT ["java", "-jar", "Gaps-0.0.4.jar"]