FROM openjdk:17-oracle
WORKDIR /app
COPY build/libs/messenger-0.0.1-SNAPSHOT.jar /app/messenger-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "messenger-0.0.1-SNAPSHOT.jar"]