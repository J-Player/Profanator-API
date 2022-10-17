FROM openjdk:11-jdk
ARG JAR_FILE=out/artifacts/Profanator_API_main_jar/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]