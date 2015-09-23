FROM debian:jessie
MAINTAINER Tomitribe

# reloading sources
RUN apt-get update
RUN apt-get upgrade

# essential
RUN apt-get install build-essential -y

# Installing java 8
RUN echo 'deb http://http.debian.net/debian jessie-backports main' >> /etc/apt/sources.list
RUN apt-get update
RUN apt-get install openjdk-8-jdk -y

# reloading nodejs
RUN apt-get install curl -y
RUN curl -sL https://deb.nodesource.com/setup_0.12 | bash -
RUN apt-get install nodejs -y

RUN mkdir -p /opt/
WORKDIR /opt/

# maven
RUN curl -O http://apache.parentingamerica.com/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
RUN tar xvzf apache-maven-3.3.3-bin.tar.gz
RUN rm apache-maven-3.3.3-bin.tar.gz
ENV PATH /opt/apache-maven-3.3.3/bin:$PATH

# one time load jars
RUN mkdir -p /opt/load/
COPY pom.xml /opt/load/
COPY static/package.json /opt/load/
WORKDIR /opt/load/
RUN mvn clean install
RUN npm install -g || true
RUN npm install -g gulp || true

WORKDIR /opt
RUN rm -Rf /opt/load

RUN mkdir -p /opt/project
VOLUME /opt/project

COPY container_build.sh /opt/
RUN chmod +x /opt/container_build.sh

CMD /opt/container_build.sh
