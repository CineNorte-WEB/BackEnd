# jdk17 Image Start
FROM openjdk:17

# 작업 디렉토리 설정
WORKDIR /app

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# JAR 파일 복사
COPY ${JAR_FILE} app.jar

# 실행 명령어 (외부 설정 파일 경로를 명시적으로 지정)
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/app/config/application.yml"]
