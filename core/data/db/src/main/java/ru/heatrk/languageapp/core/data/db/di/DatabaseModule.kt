package ru.heatrk.languageapp.core.data.db.di

import androidx.room.Room
import ru.heatrk.languageapp.core.data.db.AppDatabase
import scout.definition.Registry

fun Registry.useDatabaseBeans() {
    singleton<AppDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = AppDatabase.NAME
        ).build()
    }
}