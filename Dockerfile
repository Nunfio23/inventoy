FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY inventory.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java","-jar","app.jar"]
