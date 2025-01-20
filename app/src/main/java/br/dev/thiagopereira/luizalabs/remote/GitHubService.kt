package br.dev.thiagopereira.luizalabs.remote

import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.model.GitHubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") itemsPerPage: Int = 30
    ): Response<GitHubSearchResponse>

    @GET("/repos/{owner}/{repo}/pulls")
    suspend fun getPullRequests(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String = "all",
        @Query("per_page") itemsPerPage: Int = 30,
        @Query("page") page: Int = 1
    ): Response<List<PullRequestEntity>>

}