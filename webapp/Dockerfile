FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -B dependency:go-offline

COPY src ./src
RUN ./mvnw -B -DskipTests package && mv target/*.jar target/app.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/app.jar app.jar

RUN touch .env

EXPOSE 5763

HEALTHCHECK --interval=10s --timeout=5s --start-period=40s --retries=5 \
    CMD wget -q --spider http://localhost:5763/login || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
