# Build stage
FROM maven AS build
COPY . .
RUN mvn clean package -DskipTests

# Package stage
FROM eclipse-temurin:20
COPY --from=build /target/note-0.0.1-SNAPSHOT.jar note.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","note.jar"]