FROM openjdk:13
COPY target/*.jar diplom-orchestrator.jar
ENTRYPOINT java -jar diplom-orchestrator.jar
EXPOSE 8080