FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} conferencescheduler.jar
ENTRYPOINT ["java","-jar","/conferencescheduler.jar"]