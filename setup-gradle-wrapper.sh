#!/bin/bash

# Скрипт для автоматической настройки Gradle Wrapper
# Запустите этот скрипт один раз для подготовки проекта к сборке

echo "Настройка Gradle Wrapper..."

# Создаем директорию если её нет
mkdir -p gradle/wrapper

# Пытаемся скачать gradle-wrapper.jar
echo "Попытка скачать gradle-wrapper.jar..."
wget https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar \
     -O gradle/wrapper/gradle-wrapper.jar 2>/dev/null

if [ ! -f gradle/wrapper/gradle-wrapper.jar ]; then
    echo "Не удалось скачать gradle-wrapper.jar"
    echo ""
    echo "Пожалуйста, скачайте файл вручную:"
    echo "1. Перейдите на: https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar"
    echo "2. Сохраните файл как gradle/wrapper/gradle-wrapper.jar"
    echo ""
    echo "Или используйте Android Studio - она создаст файл автоматически"
    exit 1
fi

echo "✓ gradle-wrapper.jar установлен успешно!"
echo ""
echo "Теперь вы можете собрать APK:"
echo "  ./gradlew assembleDebug"
echo ""
echo "APK будет находиться в:"
echo "  app/build/outputs/apk/debug/app-debug.apk"
