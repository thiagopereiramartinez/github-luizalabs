package br.dev.thiagopereira.luizalabs.viewmodel

import androidx.paging.testing.asSnapshot
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.thiagopereira.luizalabs.data.RepositorioDataFactory
import br.dev.thiagopereira.luizalabs.db.dao.RepositorioDao
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
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
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.argumentCaptor
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
    private val intCaptor = argumentCaptor<Int>()

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = RepositoriosViewModel(gitHubRepository, remoteMediatorFactory)
    }

    @Test
    fun teste() = runTest {

        val repositorios = RepositorioDataFactory.createMany(150)

        wheneverBlocking {
            gitHubService.searchRepositories(
                query = anyString(),
                page = anyInt(),
                sort = anyString(),
                order = anyString(),
                itemsPerPage = anyInt()
            )
        }.thenReturn(
            Response.success(
                GitHubSearchResponse(
                    totalCount = repositorios.size,
                    items = repositorios
                ), mockPaginationHeaders()
            )
        )

        val itemsSnapshot = mutableListOf<RepositorioEntity>()
        viewModel.repositories.asSnapshot {
            scrollTo(1)
        }.toCollection(itemsSnapshot)

        assertEquals(repositorios, itemsSnapshot)

    }

    @After
    fun tearDown() = runTest {
        repositorioDao.clearAll()
    }

}