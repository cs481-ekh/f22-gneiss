from ubuntu:latest

RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive apt-get -y install mysql-server
RUN usermod -d /var/lib/mysql/ mysql

COPY build.sh /
COPY data/init.sql /

CMD /build.sh
