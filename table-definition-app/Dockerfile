FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG JAR_FILE=/workspace/app/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]