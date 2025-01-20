package br.dev.thiagopereira.luizalabs.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity

@Dao
interface PullRequestDao {

    @Upsert
    suspend fun upsert(vararg pullRequest: PullRequestEntity)

    @Query("""
        SELECT * FROM pull_requests WHERE repositorioId = :repositorioId ORDER BY createdAt DESC
    """)
    fun pagingSource(repositorioId: Long): PagingSource<Int, PullRequestEntity>

    @Query("""
        DELETE FROM pull_requests WHERE repositorioId = :repositorioId
    """)
    suspend fun clear(repositorioId: Long)

    @Query("""
        DELETE FROM pull_requests
    """)
    suspend fun clearAll()

}