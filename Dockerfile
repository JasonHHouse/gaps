FROM maven:3.6.0-jdk-11-slim

RUN mkdir -p /usr/src/app/src
WORKDIR /usr/src/app

COPY startup.sh mvnw mvnw.cmd pom.xml /usr/src/app/

COPY src /usr/src/app/src/

RUN chmod +x /usr/src/app/startup.sh

#RUN mvn clean install

CMD "/usr/src/app/startup.sh"
