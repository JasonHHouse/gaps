FROM openjdk:11.0.3-jre-slim

EXPOSE 32400

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY GapsWeb/target/GapsWeb-0.0.5.jar /usr/src/app/

ENTRYPOINT ["java", "-jar", "GapsWeb-0.0.5.jar"]