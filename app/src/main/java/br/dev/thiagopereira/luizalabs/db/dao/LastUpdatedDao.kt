package br.dev.thiagopereira.luizalabs.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import br.dev.thiagopereira.luizalabs.db.model.LastUpdatedEntity

@Dao
interface LastUpdatedDao {

    @Upsert
    suspend fun upsert(lastUpdated: LastUpdatedEntity)

    @Query("SELECT lastUpdated FROM last_updated WHERE entity = :entity")
    suspend fun getLastUpdated(entity: String): Long?

}