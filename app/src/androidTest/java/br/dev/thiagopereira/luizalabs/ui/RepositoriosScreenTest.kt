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
import br.dev.thiagopereira.luizalabs.model.GitHubSearchResponse
import br.dev.thiagopereira.luizalabs.ui.main.MainActivity
import br.dev.thiagopereira.luizalabs.ui.repositorios.GitHubRepositoriesScreen
import br.dev.thiagopereira.luizalabs.utils.FakeImageRule
import br.dev.thiagopereira.luizalabs.utils.enqueue
import br.dev.thiagopereira.luizalabs.utils.fromAssets
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RepositoriosScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val fakeImageRule = FakeImageRule()

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testListagemRepositorios() {
        val response = fromAssets<GitHubSearchResponse>("repositorios.json")
        mockWebServer.enqueue(response)

        with(composeTestRule) {
            activity.setContent {
                GitHubRepositoriesScreen { }
            }

            response.items.forEach { item ->
                val key = "repositorioId:${item.id}"

                // Fazer scroll até o item
                onNode(hasScrollAction()).performScrollToNode(
                    hasTestTag(key)
                )

                // Verificar se o nome do repositório está sendo exibido
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("name") and hasText(item.name))
                    .assertIsDisplayed()

                // Verificar se o nome do usuário está sendo exibido
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("owner") and hasText(item.owner.login))
                    .assertIsDisplayed()

                // Verificar se a descrição do repositório está sendo exibida
                if (item.description != null) {
                    onNodeWithTag(key, useUnmergedTree = true)
                        .onChildren()
                        .filterToOne(hasTestTag("description") and hasText(item.description!!))
                        .assertIsDisplayed()
                }

                // Verificar se a quantidade de forks está sendo exibida
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("forks"))
                    .onChildren()
                    .filterToOne(hasText(item.forksCount.toString()))
                    .assertIsDisplayed()

                // Verificar se a quantidade de estrelas está sendo exibida
                onNodeWithTag(key, useUnmergedTree = true)
                    .onChildren()
                    .filterToOne(hasTestTag("stars"))
                    .onChildren()
                    .filterToOne(hasText(item.stargazersCount.toString()))
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