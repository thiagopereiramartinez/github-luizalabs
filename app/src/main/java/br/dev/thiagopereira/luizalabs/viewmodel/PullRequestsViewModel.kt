package br.dev.thiagopereira.luizalabs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.repository.GitHubRepository
import br.dev.thiagopereira.luizalabs.repository.PullRequestsRemoteMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = PullRequestsViewModel.Factory::class)
class PullRequestsViewModel @AssistedInject constructor(
    gitHubRepository: GitHubRepository,
    remoteMediatorFactory: PullRequestsRemoteMediator.Factory,
    @Assisted repository: RepositorioEntity
) : ViewModel() {

    val pagingConfig = PagingConfig(pageSize = 30)

    @OptIn(ExperimentalPagingApi::class)
    private val _pullRequests = Pager(
        config = pagingConfig,
        remoteMediator = remoteMediatorFactory.create(repository)
    ) {
        gitHubRepository.pullRequestPagingSource(repository)
    }.flow
        .cachedIn(viewModelScope)

    val pullRequests = _pullRequests

    @AssistedFactory
    interface Factory {
        fun create(repository: RepositorioEntity): PullRequestsViewModel
    }

}