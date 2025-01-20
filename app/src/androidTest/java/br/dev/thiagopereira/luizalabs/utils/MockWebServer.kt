package br.dev.thiagopereira.luizalabs.utils

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

fun MockWebServer.dispatcher(vararg routes: Pair<String, String>, `else`: () -> MockResponse = { MockResponse().setResponseCode(400) } ) {
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            request.path ?: return `else`()

            return routes.firstOrNull { r -> request.path!!.contains(r.first) }?.second?.mockResponse() ?: `else`()
        }
    }
}

fun MockWebServer.dispatcherFromAssets(vararg routes: Pair<String, String>) {
    dispatcher(*routes.map { r ->
        r.first to fromAssetsRaw(r.second)
    }.toTypedArray())
}

fun String.mockResponse() = MockResponse()
    .setHeader("Content-Type", "application/json")
    .setBody(this)
    .setResponseCode(200)