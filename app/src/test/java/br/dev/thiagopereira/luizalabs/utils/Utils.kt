package br.dev.thiagopereira.luizalabs.utils

import okhttp3.Headers

fun mockPaginationHeaders(
    hasNextPage: Boolean = true,
    hasLastPage: Boolean = true,
    hasPrevPage: Boolean = false,
    hasFirstPage: Boolean = false
): Headers {

    val links = mutableListOf<String>()
    if (hasNextPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=2>; rel=\"next\"")
    }
    if (hasLastPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=6>; rel=\"last\"")
    }
    if (hasPrevPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=4>; rel=\"prev\"")
    }
    if (hasFirstPage) {
        links.add("<https://api.github.com/repositories/3432266/pulls?state=open&per_page=30&page=1>; rel=\"first\"")
    }

    return Headers.Builder().set("link", links.joinToString(", ")).build()
}