package br.dev.thiagopereira.luizalabs.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import br.dev.thiagopereira.luizalabs.db.model.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Upsert
    suspend fun upsert(remoteKey: RemoteKeyEntity)

    @Query("""
        SELECT * FROM remote_keys WHERE query = :query
    """)
    suspend fun getRemoteKey(query: String): RemoteKeyEntity

    @Query("""
        DELETE FROM remote_keys WHERE query = :query
    """)
    suspend fun deleteByQuery(query: String)

}