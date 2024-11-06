package cdu278.githubdownloader.common.typeconverter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {

    @TypeConverter
    fun toMillis(instant: Instant): Long = instant.toEpochMilliseconds()

    @TypeConverter
    fun fromMillis(millis: Long): Instant = Instant.fromEpochMilliseconds(millis)
}