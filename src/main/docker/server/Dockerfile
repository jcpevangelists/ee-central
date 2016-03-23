FROM debian:jessie

RUN echo 'deb http://http.debian.net/debian jessie-backports main' >> /etc/apt/sources.list
RUN apt-get update                                                              
RUN apt-get install openjdk-8-jre-headless -y 

COPY apache-tomee/ /opt/tomee
WORKDIR /opt/

ENV github_atoken $github_atoken
                                                                                
CMD ./tomee/bin/catalina.sh run
