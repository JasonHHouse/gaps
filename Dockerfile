##
# Copyright 2019 Jason H House
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
##

FROM eclipse-temurin:17.0.5_8-jre

LABEL maintainer="jh5975@gmail.com"
LABEL name="Jason House" 
LABEL github="https://github.com/JasonHHouse/Gaps" 

EXPOSE 8484

ENV JAR_FILE gaps.jar

ENV ENABLE_SSL false

ENV ENABLE_LOGIN false

ENV BASE_URL ""

RUN mkdir -p /usr/data && chmod 777 /usr/data

COPY movieIds.json /usr/data

RUN mkdir -p /usr/app && chmod 777 /usr/app

WORKDIR /usr/app

COPY GapsWeb/target/GapsWeb-0.10.4.jar /usr/app/gaps.jar

COPY start.sh /usr/app/

CMD ./start.sh

