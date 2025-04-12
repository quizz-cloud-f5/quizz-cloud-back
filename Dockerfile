# Fase 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Preparar dependencias para mejorar el cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Crear un usuario no root
RUN useradd -m appuser

# Copiar el jar desde la fase de build
COPY --from=build /app/target/backend-*.jar app.jar

# Asignar los permisos al usuario no root
RUN chown -R appuser:appuser /app

# Cambiar al usuario no root
USER appuser

# Exponer el puerto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
