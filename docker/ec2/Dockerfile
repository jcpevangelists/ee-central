FROM debian:jessie
MAINTAINER Thiago Veronezi

RUN echo 'deb http://http.debian.net/debian jessie-backports main' >> /etc/apt/sources.list
RUN apt-get update
RUN apt-get install -y openjdk-8-jre-headless
RUN apt-get install -y curl

RUN mkdir -p /opt
WORKDIR /opt
RUN curl -O http://apache.mirror.iweb.ca/tomee/tomee-1.7.2/apache-tomee-1.7.2-plus.tar.gz 
RUN tar xvzf apache-tomee-1.7.2-plus.tar.gz
RUN rm apache-tomee-1.7.2-plus.tar.gz
RUN rm -Rf apache-tomee-plus-1.7.2/webapps/*
COPY tomitribe-io.war apache-tomee-plus-1.7.2/webapps/ 
RUN mv apache-tomee-plus-1.7.2/webapps/tomitribe-io.war apache-tomee-plus-1.7.2/webapps/ROOT.war

CMD ./apache-tomee-plus-1.7.2/bin/catalina.sh run
