package br.dev.thiagopereira.luizalabs.viewmodel

import androidx.paging.testing.asSnapshot
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.thiagopereira.luizalabs.data.RepositorioDataFactory
import br.dev.thiagopereira.luizalabs.db.dao.RepositorioDao
import br.dev.thiagopereira.luizalabs.model.GitHubSearchResponse
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import br.dev.thiagopereira.luizalabs.repository.GitHubRepository
import br.dev.thiagopereira.luizalabs.repository.RepositoriosRemoteMediator
import br.dev.thiagopereira.luizalabs.utils.CoroutineTestRule
import br.dev.thiagopereira.luizalabs.utils.mockPaginationHeaders
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.lastValue
import org.mockito.kotlin.wheneverBlocking
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RepositoriosViewModelTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mainDispatcherRule = CoroutineTestRule()

    @Inject
    lateinit var gitHubService: GitHubService

    @Inject
    lateinit var gitHubRepository: GitHubRepository

    @Inject
    lateinit var repositorioDao: RepositorioDao

    @Inject
    lateinit var remoteMediatorFactory: RepositoriosRemoteMediator.Factory

    private lateinit var viewModel: RepositoriosViewModel
    private val intCaptor = ArgumentCaptor.forClass(Int::class.java)

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = RepositoriosViewModel(gitHubRepository, remoteMediatorFactory)
    }

    @Test
    fun `Testar carregamento inicial da pagina`() = runTest {

        val repositorios = RepositorioDataFactory.createMany(300).map {
            it.copy(language = "Kotlin")
        }.sortedByDescending { it.stargazersCount }

        val itemsPerPage = viewModel.pagingConfig.pageSize

        val maxPages = repositorios.size / itemsPerPage

        wheneverBlocking {
            gitHubService.searchRepositories(
                query = anyString(),
                page = intCaptor.capture(),
                sort = anyString(),
                order = anyString(),
                itemsPerPage = anyInt()
            )
        }.thenAnswer {
            val currentPage = intCaptor.lastValue
            val nextPage = if (currentPage < maxPages) currentPage + 1 else null

            Response.success(
                GitHubSearchResponse(
                    totalCount  = repositorios.size,
                    items = repositorios
                ), mockPaginationHeaders(nextPage = nextPage)
            )
        }
        val snapshot = viewModel.repositories.asSnapshot {  }

        assertEquals(repositorios.take(viewModel.pagingConfig.initialLoadSize), snapshot)
    }

    @After
    fun tearDown() = runTest {
        repositorioDao.clearAll()
    }

}