package ru.heatrk.languageapp.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.heatrk.languageapp.core.data.db.entities.EmptyEntity

@Database(entities = [EmptyEntity::class], version = AppDatabase.VERSION)
internal abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
        const val NAME = "APP_DB"
    }
}