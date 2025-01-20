package br.dev.thiagopereira.luizalabs.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.dev.thiagopereira.luizalabs.db.GitHubDatabase
import br.dev.thiagopereira.luizalabs.db.dao.LastUpdatedDao
import br.dev.thiagopereira.luizalabs.db.dao.RemoteKeyDao
import br.dev.thiagopereira.luizalabs.db.dao.RepositorioDao
import br.dev.thiagopereira.luizalabs.db.model.LastUpdatedEntity
import br.dev.thiagopereira.luizalabs.db.model.RemoteKeyEntity
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import br.dev.thiagopereira.luizalabs.utils.getGitHubPagination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class RepositoriosRemoteMediator @AssistedInject constructor(
    private val database: GitHubDatabase,
    private val dao: RepositorioDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val lastUpdatedDao: LastUpdatedDao,
    private val service: GitHubService,
    @Assisted private val language: String
) : RemoteMediator<Int, RepositorioEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES)

        return if (System.currentTimeMillis() - (lastUpdatedDao.getLastUpdated("repositorios") ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            lastUpdatedDao.upsert(LastUpdatedEntity("repositorios"))
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepositorioEntity>
    ): MediatorResult {

        val query = "language:$language"

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKey("repositorios", query)
                    }
                    if (remoteKey != null && remoteKey.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey?.nextKey
                }
            }

            val response = service.searchRepositories(
                query = query,
                page = loadKey ?: 1,
                itemsPerPage = state.config.pageSize
            )
            val paginationInfo = response.getGitHubPagination()
            val items = response.body()?.items ?: throw Exception("Empty response")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                    remoteKeyDao.deleteByQuery("repositorios", query)
                }
                dao.upsert(*items.toTypedArray())
                remoteKeyDao.upsert(
                    RemoteKeyEntity(
                        entity = "repositorios",
                        query = query,
                        nextKey = paginationInfo.nextPage
                    )
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = (!paginationInfo.hasNextPage || items.isEmpty())
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            MediatorResult.Error(ex)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(language: String): RepositoriosRemoteMediator
    }

}