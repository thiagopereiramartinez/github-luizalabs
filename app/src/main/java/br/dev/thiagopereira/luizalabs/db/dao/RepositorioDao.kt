package br.dev.thiagopereira.luizalabs.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity

@Dao
interface RepositorioDao {

    @Upsert
    suspend fun upsert(vararg repositorio: RepositorioEntity)

    @Query("""SELECT * FROM repositorios""")
    suspend fun getAll(): List<RepositorioEntity>

    @Query("""
        SELECT * FROM repositorios WHERE language = :language ORDER BY stargazersCount DESC
    """)
    fun pagingSource(language: String): PagingSource<Int, RepositorioEntity>

    @Query("""
        DELETE FROM repositorios
    """)
    suspend fun clearAll()

}