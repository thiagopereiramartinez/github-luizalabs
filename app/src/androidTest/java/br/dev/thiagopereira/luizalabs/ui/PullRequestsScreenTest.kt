package br.dev.thiagopereira.luizalabs.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.model.GitHubSearchResponse
import br.dev.thiagopereira.luizalabs.ui.main.MainActivity
import br.dev.thiagopereira.luizalabs.ui.pullrequests.PullRequestsScreen
import br.dev.thiagopereira.luizalabs.utils.FakeImageRule
import br.dev.thiagopereira.luizalabs.utils.dispatcherFromAssets
import br.dev.thiagopereira.luizalabs.utils.fromAssets
import br.dev.thiagopereira.luizalabs.utils.fromAssetsAsList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PullRequestsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val fakeImageRule = FakeImageRule()

    @Inject
    lateinit var mockWebServer: MockWebServer

    private lateinit var repositorio: RepositorioEntity

    @Before
    fun setup() {
        hiltRule.inject()

        mockWebServer.dispatcherFromAssets(
            "/search/repositories" to "repositorios.json",
            "/repos/JetBrains/kotlin/pulls" to "pull-requests.json"
        )

        val repositorios = fromAssets<GitHubSearchResponse>("repositorios.json").items
        repositorio = repositorios.find {
            it.fullName == "JetBrains/kotlin"
        }!!
    }

    @Test
    fun testListagemPullRequests() = runTest {
        val response = fromAssetsAsList<PullRequestEntity>("pull-requests.json")

        with(composeTestRule) {
            activity.setContent {
                PullRequestsScreen(
                    repository = repositorio
                )
            }

            runBlocking { 500L }

            response.forEach { pr ->
                val key = "prId:${pr.id}"

                // Fazer scroll até o item
                onNode(hasScrollAction()).performScrollToNode(
                    hasTestTag(key)
                )

                // Verificar se o título do pull request está sendo exibido
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("title") and hasText(pr.title))
                    .assertIsDisplayed()

                // Verificar se o corpo do pull request está sendo exibido
                if (pr.body != null) {
                    onNodeWithTag(key, useUnmergedTree = true)
                        .onChildren()
                        .filterToOne(hasTestTag("body") and hasText(pr.body!!))
                        .assertIsDisplayed()
                }

                // Verificar se o nome do usuário está sendo exibido
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("owner") and hasText(pr.user.login))
                    .assertIsDisplayed()

                // Verificar se a imagem do avatar do usuário está sendo exibida
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("avatar"))
                    .assertIsDisplayed()
            }
        }
    }

}