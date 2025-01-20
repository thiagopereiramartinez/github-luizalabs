package br.dev.thiagopereira.luizalabs.model

data class GitHubPagination(
    val previousPage: Int?,
    val nextPage: Int?,
    val lastPage: Int?,
    val firstPage: Int?
) {

    val hasNextPage: Boolean
        get() = nextPage != null

}