# Dockerfile universal para microservicios Spring Boot
FROM maven:3.9.8-eclipse-temurin-17-alpine AS builder

WORKDIR /app
# Copia el pom.xml específico de ESTE microservicio
COPY pom.xml .
# Descarga todas las dependencias (capa cacheada)
RUN mvn dependency:go-offline -B

# Copia el código fuente específico de ESTE microservicio
COPY src ./src
# Compila y empaqueta ESTE microservicio
RUN mvn clean package -DskipTests

# Imagen final minimalista
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia el JAR específico generado
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto (se configura en application.yml de cada microservicio)
EXPOSE 8080

# Ejecuta el microservicio específico
ENTRYPOINT ["java", "-jar", "app.jar"]