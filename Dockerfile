FROM openjdk:8-jdk
ARG JAR_FILE=/build/libs/*jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
#현재 MYSQL 연결을 localhost 로 해놨음
#도커는 MYSQL 연결 정보를 찾을 때 localhost를 찾을 수 없음