FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew

RUN ./gradlew --no-daemon dependencies || true

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache tzdata \
    && addgroup -S app \
    && adduser -S app -G app

COPY --from=builder /app/build/libs/*.jar /app/
RUN set -eux; \
    JAR="$(ls /app/*.jar | grep -v -- '-plain\.jar$' | head -n 1)"; \
    mv "$JAR" /app/app.jar; \
    rm -f /app/*-plain.jar

ENV TZ=Asia/Seoul
ARG SERVER_PORT=8080
ENV SERVER_PORT=${SERVER_PORT}

EXPOSE 8080
USER app

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
