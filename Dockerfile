FROM openjdk:17-jdk-slim
WORKDIR /game
COPY build/libs/game-0.0.1-SNAPSHOT.jar game.jar
CMD ["java", "-jar", "game.jar"]