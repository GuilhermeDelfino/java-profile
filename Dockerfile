# syntax=docker/dockerfile:1

# ---- Build ----
FROM eclipse-temurin:26-jdk AS builder
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -q

COPY src src

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests -q

# ---- Layer extraction ----
FROM eclipse-temurin:26-jdk AS layers
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# ---- Runtime ----
FROM eclipse-temurin:26-jre
WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=layers /app/dependencies/ ./
COPY --from=layers /app/spring-boot-loader/ ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
