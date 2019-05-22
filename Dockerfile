FROM openjdk:11.0.3-jre-slim

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY target/Gaps-0.0.4.jar /usr/src/app/

#ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "Gaps-0.0.4.jar"]
ENTRYPOINT ["java", "-jar", "Gaps-0.0.4.jar"]