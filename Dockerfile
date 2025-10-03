FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/inventory-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java","-jar","app.jar"]
