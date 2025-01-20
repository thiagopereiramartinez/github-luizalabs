package br.dev.thiagopereira.luizalabs.viewmodel

import androidx.paging.testing.asSnapshot
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.thiagopereira.luizalabs.data.PullRequestDataFactory
import br.dev.thiagopereira.luizalabs.data.RepositorioDataFactory
import br.dev.thiagopereira.luizalabs.db.dao.PullRequestDao
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import br.dev.thiagopereira.luizalabs.repository.GitHubRepository
import br.dev.thiagopereira.luizalabs.repository.PullRequestsRemoteMediator
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
class PullRequestsViewModelTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mainDispatcherRule = CoroutineTestRule()

    @Inject
    lateinit var gitHubService: GitHubService

    @Inject
    lateinit var gitHubRepository: GitHubRepository

    @Inject
    lateinit var pullRequestDao: PullRequestDao

    @Inject
    lateinit var remoteMediatorFactory: PullRequestsRemoteMediator.Factory

    private lateinit var viewModel: PullRequestsViewModel
    private val repositorio = RepositorioDataFactory.create()

    private val intCaptor = ArgumentCaptor.forClass(Int::class.java)

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = PullRequestsViewModel(gitHubRepository, remoteMediatorFactory, repositorio)
    }

    @Test
    fun `Testar carregamento inicial da pagina`() = runTest {

        val repositorios = PullRequestDataFactory.createMany(300).map {
            it.copy(repositorioId = repositorio.id)
        }.sortedByDescending { it.createdAt }

        val itemsPerPage = viewModel.pagingConfig.pageSize

        val maxPages = repositorios.size / itemsPerPage

        wheneverBlocking {
            gitHubService.getPullRequests(
                owner = anyString(),
                repo = anyString(),
                state = anyString(),
                page = intCaptor.capture(),
                itemsPerPage = anyInt()
            )
        }.thenAnswer {
            val currentPage = intCaptor.lastValue
            val nextPage = if (currentPage < maxPages) currentPage + 1 else null

            Response.success(
                repositorios, mockPaginationHeaders(nextPage = nextPage)
            )
        }
        val snapshot = viewModel.pullRequests.asSnapshot {  }

        assertEquals(repositorios.take(viewModel.pagingConfig.initialLoadSize), snapshot)
    }

    @After
    fun tearDown() = runTest {
        pullRequestDao.clearAll()
    }

}