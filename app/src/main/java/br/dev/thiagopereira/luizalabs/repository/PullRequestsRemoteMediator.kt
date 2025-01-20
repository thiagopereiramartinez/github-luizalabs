package br.dev.thiagopereira.luizalabs.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.dev.thiagopereira.luizalabs.db.GitHubDatabase
import br.dev.thiagopereira.luizalabs.db.dao.LastUpdatedDao
import br.dev.thiagopereira.luizalabs.db.dao.PullRequestDao
import br.dev.thiagopereira.luizalabs.db.dao.RemoteKeyDao
import br.dev.thiagopereira.luizalabs.db.model.LastUpdatedEntity
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.db.model.RemoteKeyEntity
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import br.dev.thiagopereira.luizalabs.utils.getGitHubPagination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PullRequestsRemoteMediator @AssistedInject constructor(
    private val database: GitHubDatabase,
    private val dao: PullRequestDao,
    private val lastUpdatedDao: LastUpdatedDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val service: GitHubService,
    @Assisted private val repositorio: RepositorioEntity
) : RemoteMediator<Int, PullRequestEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES)

        return if (System.currentTimeMillis() - (lastUpdatedDao.getLastUpdated("pull_requests-${repositorio.id}") ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            lastUpdatedDao.upsert(LastUpdatedEntity("pull_requests-${repositorio.id}"))
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PullRequestEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.getRemoteKey(
                            entity = "pull_requests",
                            query = repositorio.id.toString()
                        )
                    }
                    if (remoteKey != null && remoteKey.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey?.nextKey
                }
            }

            val response = service.getPullRequests(
                owner = repositorio.owner.login,
                repo = repositorio.name,
                page = loadKey ?: 1,
                itemsPerPage = state.config.pageSize
            )
            val paginationInfo = response.getGitHubPagination()
            val items = response.body() ?: throw Exception("Empty response")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clear(repositorio.id)
                    remoteKeyDao.deleteByQuery("pull_requests", repositorio.id.toString())
                }
                dao.upsert(*items.map { it.copy(repositorioId = repositorio.id) }.toTypedArray())
                remoteKeyDao.upsert(
                    RemoteKeyEntity(
                        entity = "pull_requests",
                        query = repositorio.id.toString(),
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
        fun create(repositorio: RepositorioEntity): PullRequestsRemoteMediator
    }

}