package com.reminder.app.data

enum class RepeatType(val value: Int) {
    NONE(0),
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4),
    WEEKDAYS(5),
    WEEKENDS(6);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}
