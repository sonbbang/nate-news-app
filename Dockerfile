# Step 1: Base Image
FROM eclipse-temurin:17-jdk AS build

# Step 2: 프로젝트 jar 복사
COPY build/libs/nate-news-app-0.0.1-SNAPSHOT.jar app.jar

# Step 3: 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]