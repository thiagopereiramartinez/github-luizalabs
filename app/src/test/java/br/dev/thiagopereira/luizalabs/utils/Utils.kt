package br.dev.thiagopereira.luizalabs.utils

import okhttp3.Headers

fun mockPaginationHeaders(
    nextPage: Int? = 3,
    hasNextPage: Boolean = true,
    lastPage: Int = 6,
    hasLastPage: Boolean = true,
    prevPage: Int? = 1,
    hasPrevPage: Boolean = false,
    firstPage: Int = 1,
    hasFirstPage: Boolean = false
): Headers {

    val links = mutableListOf<String>()
    if (hasNextPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=$nextPage>; rel=\"next\"")
    }
    if (hasLastPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=$lastPage>; rel=\"last\"")
    }
    if (hasPrevPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=$prevPage>; rel=\"prev\"")
    }
    if (hasFirstPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=$firstPage>; rel=\"first\"")
    }

    return Headers.Builder().set("link", links.joinToString(", ")).build()
}