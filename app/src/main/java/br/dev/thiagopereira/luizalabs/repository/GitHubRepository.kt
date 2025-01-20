package br.dev.thiagopereira.luizalabs.repository

import br.dev.thiagopereira.luizalabs.db.dao.PullRequestDao
import br.dev.thiagopereira.luizalabs.db.dao.RepositorioDao
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import javax.inject.Inject

data class GitHubRepository @Inject constructor(
    private val repositorioDao: RepositorioDao,
    private val pullRequestDao: PullRequestDao
) {

    fun repositorioPagingSource(language: String) = repositorioDao.pagingSource(language)

    fun pullRequestPagingSource(repositorio: RepositorioEntity) = pullRequestDao.pagingSource(repositorio.id)

}
