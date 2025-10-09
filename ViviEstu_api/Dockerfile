# -------- Etapa de construcción --------
# Imagen base con Java 21
FROM eclipse-temurin:21-jdk AS build
# Definir directorio de trabajo
WORKDIR /app
# Copiar código fuente al contenedor
COPY . .
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# -------- Imagen final --------
FROM eclipse-temurin:21-jdk
# Definir directorio de trabajo
WORKDIR /app
# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar
# Exponer el puerto de la aplicación
EXPOSE 8080
# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]