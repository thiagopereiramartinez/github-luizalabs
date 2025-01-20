package br.dev.thiagopereira.luizalabs.ui.pullrequests

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.ui.components.ScrollToTop
import br.dev.thiagopereira.luizalabs.viewmodel.PullRequestsViewModel

@Composable
fun PullRequestsScreen(
    modifier: Modifier = Modifier,
    repository: RepositorioEntity,
    viewModel: PullRequestsViewModel = hiltViewModel(
        creationCallback = { factory: PullRequestsViewModel.Factory -> factory.create(repository) },
        key = repository.id.toString()
    )
) {
    val pullRequests = viewModel.pullRequests.collectAsLazyPagingItems()

    PullRequestsContent(
        modifier = modifier,
        lazyPagingItems = pullRequests
    )
}

@Composable
private fun PullRequestsContent(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<PullRequestEntity>
) {
    val context = LocalContext.current

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
            val pullRequest = lazyPagingItems[it] ?: return@items

            PullRequestListItem(
                pullRequest = pullRequest
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(pullRequest.link)
                }
                context.startActivity(intent)
            }
        }
    }
    ScrollToTop(
        listState = listState
    )
}