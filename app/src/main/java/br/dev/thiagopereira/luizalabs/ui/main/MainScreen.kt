package br.dev.thiagopereira.luizalabs.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.dev.thiagopereira.luizalabs.R
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.ui.pullrequests.PullRequestsScreen
import br.dev.thiagopereira.luizalabs.ui.repositorios.GitHubRepositoriesScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator<RepositorioEntity>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = when (navigator.currentDestination?.pane) {
                        ListDetailPaneScaffoldRole.Detail -> {
                            val repo = navigator.currentDestination?.content
                            repo?.name ?: stringResource(R.string.github_luiza_labs)
                        }
                        else -> stringResource(R.string.github_luiza_labs)
                    }
                    Text(
                        text = title
                    )
                },
                navigationIcon = {
                    if (navigator.canNavigateBack()) {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }

        ListDetailPaneScaffold(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    GitHubRepositoriesScreen { repo ->
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, repo)
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    val repo = navigator.currentDestination?.content ?: return@AnimatedPane

                    PullRequestsScreen(repository = repo)
                }
            }
        )
    }
}