# -------- Etapa de construcción --------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar el pom.xml y descargar dependencias primero (para cachear)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del proyecto
COPY src ./src

# Construir el JAR sin ejecutar tests
RUN mvn clean package -DskipTests

# -------- Imagen final --------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
