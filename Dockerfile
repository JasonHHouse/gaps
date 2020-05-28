FROM openjdk:11.0.7-jre-slim

MAINTAINER jh5975@gmail.com

EXPOSE 32400

ENV JAR_FILE gaps.jar

ENV ENABLE_SSL false

ENV ENABLE_LOGIN false

RUN mkdir -p /usr/data && chmod 777 /usr/data

COPY movieIds.json /usr/data

RUN mkdir -p /usr/app && chmod 777 /usr/data

WORKDIR /usr/app

COPY GapsWeb/target/GapsWeb-0.4.1.jar /usr/app/gaps.jar

COPY start.sh /usr/app/

CMD ./start.sh

##Figure out how to do Unraid configuration
#docker build -f Dockerfile -t gaps-dev .
#docker run -p 8484:8484 --env ENABLE_SSL=true --env ENABLE_LOGIN=true --name gaps-dev -v /home/jason/gaps:/usr/data:Z gaps-dev
#docker run -p 8484:8484 --env ENABLE_SSL=true --name gaps-dev -v /home/jason/gaps:/usr/data:Z gaps-dev
#docker run -p 8484:8484 --env ENABLE_LOGIN=true --name gaps-dev -v /home/jason/gaps:/usr/data:Z gaps-dev
#docker run -p 8484:8484 --name gaps-dev -v /home/jason/gaps:/usr/data:Z gaps-dev