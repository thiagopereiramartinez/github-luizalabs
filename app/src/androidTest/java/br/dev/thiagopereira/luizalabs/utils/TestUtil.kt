package br.dev.thiagopereira.luizalabs.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

inline fun <reified T> fromAssets(fileName: String): T {
    val assets = InstrumentationRegistry.getInstrumentation().context.resources.assets
    val file = assets.open(fileName)
    val json = file.bufferedReader().readText()

    return file.use {
        Gson().fromJson(json, T::class.java)
    }
}

inline fun <reified T> fromAssetsAsList(fileName: String): List<T> {
    val assets = InstrumentationRegistry.getInstrumentation().context.resources.assets
    val file = assets.open(fileName)
    val json = file.bufferedReader().readText()

    return file.use {
        Gson().fromJson(json, object : TypeToken<List<T>>(){}.type)
    }
}

fun fromAssetsRaw(fileName: String): String {
    val assets = InstrumentationRegistry.getInstrumentation().context.resources.assets
    val file = assets.open(fileName)
    val json = file.bufferedReader().readText()

    return file.use {
        json
    }
}

inline fun <reified T> MockWebServer.enqueue(data: T) {
    enqueue(
        MockResponse()
            .setHeader("Content-Type", "application/json")
            .setBody(Gson().toJson(data, object : TypeToken<T>(){}.type))
    )
}
