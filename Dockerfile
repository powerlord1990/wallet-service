# Используем базовый образ с JDK
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл JAR в контейнер
COPY target/wallet-service-1.0.0-SNAPSHOT.jar /app/wallet-service.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "wallet-service.jar"]