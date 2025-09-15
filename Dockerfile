FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

RUN chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY . /app

RUN ./gradlew build -x test --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "build/libs/dailyNews-0.0.1-SNAPSHOT.jar"]
