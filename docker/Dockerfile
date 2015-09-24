FROM debian:jessie
MAINTAINER Tomitribe

# reloading sources
RUN apt-get update
RUN apt-get upgrade

# essential
RUN apt-get install build-essential -y
RUN apt-get install curl -y

# Installing java 8
RUN echo 'deb http://http.debian.net/debian jessie-backports main' >> /etc/apt/sources.list
RUN apt-get update
RUN apt-get install openjdk-8-jre-headless -y

RUN mkdir -p /opt/
WORKDIR /opt/

# maven
RUN curl -O http://apache.parentingamerica.com/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
RUN tar xvzf apache-maven-3.3.3-bin.tar.gz
RUN rm apache-maven-3.3.3-bin.tar.gz
ENV PATH /opt/apache-maven-3.3.3/bin:$PATH

# one time load jars
RUN mkdir -p load
COPY pom.xml /opt/load/
WORKDIR /opt/load/
RUN mvn clean install
WORKDIR /opt/
RUN rm -Rf load/

# reloading nodejs
RUN curl -sL https://deb.nodesource.com/setup_0.12 | bash - > /dev/null 2>&1
RUN apt-get install nodejs -y
RUN npm install -g gulp@3.9.0
RUN npm install -g gulp-jade@1.1.0
RUN npm install -g gulp-sass@2.0.4
RUN npm install -g gulp-autoprefixer@2.3.1
RUN npm install -g gulp-uglify@1.4.0
RUN npm install -g gulp-sourcemaps@1.5.2
RUN npm install -g gulp-concat@2.6.0
RUN npm install -g sprity@1.0.7
RUN npm install -g gulp-if@1.2.5
RUN npm install -g del@2.0.0
RUN npm install -g gulp-sync@0.1.4
RUN npm install -g gulp-watch@4.3.5
RUN npm install -g marked@0.3.5
RUN npm install -g jstransformer-marked@1.0.1
RUN npm install -g lwip@0.0.7

COPY container_build.sh /opt/
RUN chmod u+x /opt/container_build.sh
CMD /opt/container_build.sh
