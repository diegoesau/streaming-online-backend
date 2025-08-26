FROM maven:3.9.8-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . /build
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
COPY --from=builder /build/target/*.jar /app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENTRYPOINT ["java","-jar","/app.jar"]
