package ru.heatrk.languageapp.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmptyEntity(
    @PrimaryKey
    val id: String
)
