# Этап сборки
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Копируем pom.xml для кэширования зависимостей
COPY pom.xml .

# Скачиваем зависимости (кэшируется если pom.xml не изменился)
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение, пропуская тесты
RUN mvn clean package -DskipTests -B

# Этап production
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем jar из этапа сборки
COPY --from=build /app/target/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Настройки JVM для оптимизации
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Запуск приложения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]