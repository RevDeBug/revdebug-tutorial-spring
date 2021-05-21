#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
ARG REVDEBUG_RECORD_SERVER_ADDRESS_ARG=127.0.0.1
ENV REVDEBUG_RECORD_SERVER_ADDRESS=$REVDEBUG_RECORD_SERVER_ADDRESS_ARG
RUN apt-get update
RUN apt-get install -y wget
COPY src /home/app/src
COPY pom.xml /home/app
COPY .git /home/app/.git
RUN wget -O /home/app/revdebug-agent.tar.gz https://nexus.revdebug.com/repository/files/agent/revdebug-agent-6.0.15.tar.gz
RUN mkdir /home/app/agent
RUN tar -C /home/app/agent -xf /home/app/revdebug-agent.tar.gz
RUN ls /home/app/agent
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
ENV SW_AGENT_NAME=InvoiceJava
COPY --from=build /home/app/target/revdebug-tutorial-spring-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
COPY --from=build /home/app/agent /usr/local/lib/agent
CMD java -javaagent:/usr/local/lib/agent/skywalking-agent.jar -jar /usr/local/lib/demo.jar