package br.dev.thiagopereira.luizalabs.db.model

import androidx.room.Entity

@Entity(
    tableName = "remote_keys",
    primaryKeys = ["entity", "query"]
)
data class RemoteKeyEntity(
    val entity: String,
    val query: String,
    val nextKey: Int?
)