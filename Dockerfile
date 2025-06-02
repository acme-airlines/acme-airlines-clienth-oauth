# Stage 1: Compilar el JAR con Maven (Java 17 con OpenJDK)
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias (caché)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Crear la imagen final con el JAR compilado (Java 17 con Eclipse Temurin)
FROM openjdk-17-slim

WORKDIR /app

# Copiar el JAR generado desde la etapa “build”
COPY --from=build /app/target/oauth-0.0.1-SNAPSHOT.jar oauth-ms.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "oauth-ms.jar"]
