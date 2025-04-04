# Fase 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar archivos y compilar
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiar el jar generado en la fase de build
COPY --from=build /app/target/backend-*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
