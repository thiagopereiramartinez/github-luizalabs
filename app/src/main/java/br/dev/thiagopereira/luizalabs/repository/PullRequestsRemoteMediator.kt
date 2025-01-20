package br.dev.thiagopereira.luizalabs.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.dev.thiagopereira.luizalabs.db.GitHubDatabase
import br.dev.thiagopereira.luizalabs.db.dao.PullRequestDao
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import br.dev.thiagopereira.luizalabs.utils.getGitHubPagination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@OptIn(ExperimentalPagingApi::class)
class PullRequestsRemoteMediator @AssistedInject constructor(
    private val database: GitHubDatabase,
    private val dao: PullRequestDao,
    private val service: GitHubService,
    @Assisted private val repositorio: RepositorioEntity
) : RemoteMediator<Int, PullRequestEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PullRequestEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    lastItem.id
                    state.pages.size + 1
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
                dao.upsert(*items.map { it.copy(repositorioId = repositorio.id) }.toTypedArray())
            }

            MediatorResult.Success(
                endOfPaginationReached = !paginationInfo.hasNextPage
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