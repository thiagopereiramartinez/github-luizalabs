package br.dev.thiagopereira.luizalabs.utils

import androidx.test.platform.app.InstrumentationRegistry
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.test.FakeImage
import coil3.test.FakeImageLoaderEngine
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class FakeImageRule : TestRule {

    @OptIn(DelicateCoilApi::class)
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                val engine = FakeImageLoaderEngine.Builder()
                    .default(FakeImage())
                    .build()

                val imageLoader = ImageLoader.Builder(InstrumentationRegistry.getInstrumentation().context)
                    .components { add(engine) }
                    .build()

                SingletonImageLoader.setUnsafe(imageLoader)

                base?.evaluate()
            }
        }
    }

}