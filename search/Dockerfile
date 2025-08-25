FROM openjdk:17-jdk-alpine AS builder
COPY . /build
WORKDIR /build
RUN ./mvnw package

FROM openjdk:17-jdk-alpine
COPY --from=builder /build/target/*.jar /app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENTRYPOINT ["java","-jar","/app.jar"]
