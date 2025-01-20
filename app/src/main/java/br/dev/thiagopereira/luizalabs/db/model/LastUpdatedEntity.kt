package br.dev.thiagopereira.luizalabs.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "last_updated"
)
data class LastUpdatedEntity(
    @PrimaryKey
    val entity: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
