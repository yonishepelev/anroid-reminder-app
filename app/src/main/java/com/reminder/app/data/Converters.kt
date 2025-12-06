package com.reminder.app.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromRepeatType(value: RepeatType): Int {
        return value.value
    }

    @TypeConverter
    fun toRepeatType(value: Int): RepeatType {
        return RepeatType.fromInt(value)
    }
}
