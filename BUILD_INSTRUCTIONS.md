# Инструкция по сборке APK

## Требования

1. **Android Studio** (рекомендуется) или **Android SDK**
2. **JDK 17 или выше**

## Способ 1: Android Studio (самый простой)

1. Установите [Android Studio](https://developer.android.com/studio)
2. Откройте проект в Android Studio
3. Дождитесь синхронизации Gradle
4. В меню: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. После сборки нажмите **locate** в уведомлении
6. APK будет в папке `app/build/outputs/apk/debug/app-debug.apk`

## Способ 2: Командная строка (если установлен Android SDK)

### Для Linux/Mac:

```bash
# Сборка debug APK
./gradlew assembleDebug

# APK будет здесь:
# app/build/outputs/apk/debug/app-debug.apk
```

### Для Windows:

```cmd
# Сборка debug APK
gradlew.bat assembleDebug

# APK будет здесь:
# app\build\outputs\apk\debug\app-debug.apk
```

## Установка APK на телефон

### Вариант 1: Через USB

1. Включите **Режим разработчика** на телефоне:
   - Настройки → О телефоне → Нажмите 7 раз на "Номер сборки"
2. Включите **Отладку по USB**:
   - Настройки → Для разработчиков → Отладка по USB
3. Подключите телефон к компьютеру
4. Выполните:
   ```bash
   ./gradlew installDebug
   ```

### Вариант 2: Прямая установка

1. Скопируйте `app-debug.apk` на телефон (через USB, email, мессенджер и т.д.)
2. Откройте файл на телефоне
3. Разрешите установку из неизвестных источников (если потребуется)
4. Нажмите "Установить"

## Сборка подписанного Release APK

Для публикации в Google Play нужен подписанный release APK:

1. Создайте keystore (один раз):
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
   ```

2. Добавьте в `app/build.gradle`:
   ```gradle
   android {
       signingConfigs {
           release {
               storeFile file("my-release-key.jks")
               storePassword "your-password"
               keyAlias "my-key-alias"
               keyPassword "your-password"
           }
       }
       buildTypes {
           release {
               signingConfig signingConfigs.release
               minifyEnabled true
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

3. Соберите release APK:
   ```bash
   ./gradlew assembleRelease
   ```

## Проблемы и решения

### "SDK location not found"

Создайте файл `local.properties`:
```properties
sdk.dir=/path/to/your/Android/Sdk
```

### На Mac обычно:
```
sdk.dir=/Users/YOUR_USERNAME/Library/Android/sdk
```

### На Linux:
```
sdk.dir=/home/YOUR_USERNAME/Android/Sdk
```

### На Windows:
```
sdk.dir=C\:\\Users\\YOUR_USERNAME\\AppData\\Local\\Android\\Sdk
```

### Java version issues

Проект требует JDK 17. Проверьте версию:
```bash
java -version
```

Если версия другая, установите JDK 17.

## Размер APK

Debug APK будет около 5-10 MB.
Release APK с минификацией будет меньше - около 3-5 MB.
