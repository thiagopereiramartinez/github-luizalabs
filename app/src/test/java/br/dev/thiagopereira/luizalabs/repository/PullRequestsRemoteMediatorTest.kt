package br.dev.thiagopereira.luizalabs.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.dev.thiagopereira.luizalabs.data.PullRequestDataFactory
import br.dev.thiagopereira.luizalabs.data.RepositorioDataFactory
import br.dev.thiagopereira.luizalabs.db.dao.RepositorioDao
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import br.dev.thiagopereira.luizalabs.utils.mockPaginationHeaders
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.wheneverBlocking
import retrofit2.Response
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalPagingApi::class)
class PullRequestsRemoteMediatorTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repositorioDao: RepositorioDao

    @Inject
    lateinit var gitHubService: GitHubService

    @Inject
    lateinit var remoteMediatorFactory: PullRequestsRemoteMediator.Factory

    private lateinit var remoteMediator: PullRequestsRemoteMediator

    private val repositorio = RepositorioDataFactory.create()

    @Before
    fun setup() {
        hiltRule.inject()

        remoteMediator = remoteMediatorFactory.create(repositorio)
    }

    @Test
    fun `Testar a atualizacao de dados com sucesso e contem mais dados`() = runTest {
        wheneverBlocking {
            gitHubService.getPullRequests(
                owner = anyString(),
                repo = anyString(),
                state = anyString(),
                page = anyInt(),
                itemsPerPage = anyInt()
            )
        }.thenReturn(
            Response.success(PullRequestDataFactory.createMany(10), mockPaginationHeaders())
        )

        val pagingState = PagingState<Int, PullRequestEntity>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `Testar a atualizacao de dados com sucesso e nao ha mais dados`() = runTest {
        wheneverBlocking {
            gitHubService.getPullRequests(
                owner = anyString(),
                repo = anyString(),
                state = anyString(),
                page = anyInt(),
                itemsPerPage = anyInt()
            )
        }.thenReturn(
            Response.success(
                emptyList(), mockPaginationHeaders(hasNextPage = false)
            )
        )

        val pagingState = PagingState<Int, PullRequestEntity>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `Testar erro ao atualizar os dados`() = runTest {
        wheneverBlocking {
            gitHubService.searchRepositories(
                query = anyString(),
                page = anyInt(),
                sort = anyString(),
                order = anyString(),
                itemsPerPage = anyInt()
            )
        }.thenReturn(
            Response.error(500, "".toResponseBody())
        )

        val pagingState = PagingState<Int, PullRequestEntity>(
            listOf(),
            null,
            PagingConfig(10),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @After
    fun tearDown() = runTest {
        repositorioDao.clearAll()
    }

}