# Stage 1: Compilar el JAR con Maven
FROM maven:3.8.8-openjdk-17-slim AS build

# Directorio de trabajo para compilar
WORKDIR /app

# Copiar pom.xml y descargar dependencias (caché)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Construir la imagen final con el JAR compilado
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR compilado desde la etapa "build"
COPY --from=build /app/target/oauth-0.0.1-SNAPSHOT.jar oauth-ms.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "oauth-ms.jar"]
