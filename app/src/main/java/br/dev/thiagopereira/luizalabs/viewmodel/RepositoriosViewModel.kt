package br.dev.thiagopereira.luizalabs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.dev.thiagopereira.luizalabs.repository.GitHubRepository
import br.dev.thiagopereira.luizalabs.repository.RepositoriosRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriosViewModel @Inject constructor(
    repository: GitHubRepository,
    remoteMediatorFactory: RepositoriosRemoteMediator.Factory
) : ViewModel() {

    private val language = "Kotlin"
    var pageSize = 30

    @OptIn(ExperimentalPagingApi::class)
    private val _repositories = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = remoteMediatorFactory.create(language)
    ) {
        repository.repositorioPagingSource(language)
    }.flow
        .cachedIn(viewModelScope)

    val repositories = _repositories

}