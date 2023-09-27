FROM arm64v8/eclipse-temurin:17-jre
MAINTAINER serejka
EXPOSE 1472
COPY build/libs/first-searcher-0.0.1-SNAPSHOT.jar first-searcher-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/first-searcher-0.0.1-SNAPSHOT.jar"]
