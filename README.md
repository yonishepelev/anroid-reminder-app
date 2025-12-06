# Приложение "Напоминания" (Recurring Reminders)

[![Build APK](https://github.com/yonishepelev/anroid-reminder-app/actions/workflows/build-apk.yml/badge.svg)](https://github.com/yonishepelev/anroid-reminder-app/actions/workflows/build-apk.yml)

Android приложение для создания повторяющихся напоминаний.

## 📥 Скачать APK

APK файл собирается автоматически через GitHub Actions:

1. Перейдите в [Actions](https://github.com/yonishepelev/anroid-reminder-app/actions)
2. Выберите последний успешный build
3. Скачайте **app-debug** из секции Artifacts
4. Установите на телефон

## Возможности

- ✅ Создание, редактирование и удаление напоминаний
- ✅ Настройка даты и времени для каждого напоминания
- ✅ Повторяющиеся напоминания:
  - Не повторять (разовое напоминание)
  - Каждый день
  - Каждую неделю
  - Каждый месяц
  - Каждый год
  - По будням (Пн-Пт)
  - По выходным (Сб-Вс)
- ✅ Push-уведомления для напоминаний
- ✅ Сохранение напоминаний после перезагрузки устройства
- ✅ Локальное хранение данных (Room Database)

## Технологии

- **Язык:** Kotlin
- **Минимальная версия Android:** API 24 (Android 7.0)
- **Целевая версия:** API 34 (Android 14)
- **Архитектура:** MVVM
- **База данных:** Room
- **UI:** Material Design Components
- **Уведомления:** AlarmManager + NotificationManager

## Структура проекта

```
app/
├── data/
│   ├── Reminder.kt          # Модель данных напоминания
│   ├── RepeatType.kt        # Enum для типов повторения
│   ├── ReminderDao.kt       # DAO для работы с БД
│   ├── ReminderDatabase.kt  # Room Database
│   ├── ReminderRepository.kt # Репозиторий
│   └── Converters.kt        # Type converters для Room
├── ui/
│   ├── ReminderViewModel.kt # ViewModel для напоминаний
│   └── ReminderAdapter.kt   # Adapter для RecyclerView
├── utils/
│   └── AlarmScheduler.kt    # Планирование алармов
├── MainActivity.kt          # Главный экран со списком
├── AddEditReminderActivity.kt # Экран добавления/редактирования
├── ReminderReceiver.kt      # BroadcastReceiver для уведомлений
└── BootReceiver.kt          # Восстановление напоминаний после перезагрузки
```

## Как использовать

1. Запустите приложение
2. Нажмите на кнопку "+" для создания нового напоминания
3. Введите название и описание (опционально)
4. Выберите дату и время
5. Выберите тип повторения
6. Нажмите "Сохранить"
7. В указанное время придет уведомление

## Сборка проекта

### Автоматическая сборка (GitHub Actions)
APK собирается автоматически при каждом push. Скачать можно из Artifacts в секции Actions.

### Локальная сборка

```bash
./gradlew assembleDebug
```

APK будет в `app/build/outputs/apk/debug/app-debug.apk`

Подробные инструкции: [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)

## Разрешения

Приложение запрашивает следующие разрешения:
- `POST_NOTIFICATIONS` - для отправки уведомлений (Android 13+)
- `SCHEDULE_EXACT_ALARM` - для точного планирования напоминаний
- `USE_EXACT_ALARM` - использование точных алармов
- `RECEIVE_BOOT_COMPLETED` - восстановление напоминаний после перезагрузки
