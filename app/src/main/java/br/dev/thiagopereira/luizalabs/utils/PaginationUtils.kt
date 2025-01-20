package br.dev.thiagopereira.luizalabs.utils

import br.dev.thiagopereira.luizalabs.model.GitHubPagination
import retrofit2.Response

inline fun <reified T> Response<T>.getGitHubPagination(): GitHubPagination {
    val previousPage = getPageNumberByRel("prev")
    val nextPage = getPageNumberByRel("next")
    val lastPage = getPageNumberByRel("last")
    val firstPage = getPageNumberByRel("first")

    return GitHubPagination(
        previousPage = previousPage,
        nextPage = nextPage,
        lastPage = lastPage,
        firstPage = firstPage
    )
}

inline fun <reified T> Response<T>.getPageNumberByRel(rel: String): Int? {
    val links = headers()["link"]?.split(",")?.map { it.trim() } ?: return null
    val link = links.find { it.contains("rel=\"$rel\"") } ?: return null

    return link.getPageFromUrl()
}


fun String.getPageFromUrl(): Int? {
    val regex = """[?&]page=(\d+)""".toRegex()
    val match = regex.find(this) ?: return null
    return match.groupValues[1].toInt()
}