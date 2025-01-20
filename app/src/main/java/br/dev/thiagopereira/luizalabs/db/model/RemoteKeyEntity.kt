package br.dev.thiagopereira.luizalabs.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val query: String,
    val nextKey: Int?
)