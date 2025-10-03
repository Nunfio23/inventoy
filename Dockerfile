# Etapa 1: construir
FROM eclipse-temurin:21-jdk as build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Etapa 2: correr
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/inventory-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java","-jar","app.jar"]
