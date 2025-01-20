package br.dev.thiagopereira.luizalabs.di

import br.dev.thiagopereira.luizalabs.remote.GitHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteModule::class]
)
object TestRemoteModule {

    @Provides
    @Singleton
    fun provideMockWebServer() = MockWebServer().apply {
        start()
    }

    @Provides
    @Singleton
    fun provideRetrofit(mockWebServer: MockWebServer) =
        Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    fun provideGitHubService(retrofit: Retrofit) =
        retrofit.create(GitHubService::class.java)

}