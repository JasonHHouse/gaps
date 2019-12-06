FROM openjdk:11.0.5-jre-slim

EXPOSE 32400

RUN mkdir -p /usr/data

COPY movieIds.json /tmp

RUN mkdir -p /usr/app

WORKDIR /usr/app

COPY GapsWeb/target/GapsWeb-0.0.6.jar /usr/app/

ENTRYPOINT ["java", "-jar", "GapsWeb-0.0.6.jar"]