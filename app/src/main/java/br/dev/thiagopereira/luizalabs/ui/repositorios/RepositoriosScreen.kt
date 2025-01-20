package br.dev.thiagopereira.luizalabs.ui.repositorios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.ui.components.ScrollToTop
import br.dev.thiagopereira.luizalabs.viewmodel.RepositoriosViewModel

@Composable
fun GitHubRepositoriesScreen(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    viewModel: RepositoriosViewModel = hiltViewModel<RepositoriosViewModel>(),
    onItemClick: (RepositorioEntity) -> Unit,
) {
    val lazyPagingItems = viewModel.repositories.collectAsLazyPagingItems()

    GitHubRepositoriesContent(
        modifier = modifier,
        listState = listState,
        lazyPagingItems = lazyPagingItems,
        onItemClick = onItemClick
    )
}

@Composable
private fun GitHubRepositoriesContent(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    lazyPagingItems: LazyPagingItems<RepositorioEntity>,
    onItemClick: (RepositorioEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        when {
            lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount == 0 -> {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) {
            val repository = lazyPagingItems[it] ?: return@items

            RepositorioListItem(
                repositorio = repository
            ) {
                onItemClick(repository)
            }
        }
    }
    ScrollToTop(
        listState = listState
    )
}