package br.dev.thiagopereira.luizalabs.ui.repositorios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.viewmodel.RepositoriosViewModel

@Composable
fun GitHubRepositoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RepositoriosViewModel = hiltViewModel<RepositoriosViewModel>(),
    onItemClick: (RepositorioEntity) -> Unit,
) {
    val repositories = viewModel.repositories.collectAsLazyPagingItems()

    GitHubRepositoriesContent(
        modifier = modifier,
        repositories = repositories,
        onItemClick = onItemClick
    )
}

@Composable
private fun GitHubRepositoriesContent(
    modifier: Modifier = Modifier,
    repositories: LazyPagingItems<RepositorioEntity>,
    onItemClick: (RepositorioEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(repositories.itemCount, key = repositories.itemKey { it.id }) {
            val repository = repositories[it] ?: return@items

            RepositorioListItem(
                repositorio = repository
            ) {
                onItemClick(repository)
            }
        }
    }
}