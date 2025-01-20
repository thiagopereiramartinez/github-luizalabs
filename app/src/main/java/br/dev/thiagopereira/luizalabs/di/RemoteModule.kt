package br.dev.thiagopereira.luizalabs.di

import br.dev.thiagopereira.luizalabs.BuildConfig
import br.dev.thiagopereira.luizalabs.remote.GitHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun providesOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .addInterceptor { chain ->
                val isAuthenticated = BuildConfig.GitHubToken.startsWith("ghp_")

                chain.proceed(
                    chain.request().newBuilder()
                        .apply {
                            if (isAuthenticated) {
                                addHeader("Authorization", "Bearer ${BuildConfig.GitHubToken}")
                            }
                        }
                        .build()
                )
            }
            .build()

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.ApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun providesGitHubService(retrofit: Retrofit) =
        retrofit.create(GitHubService::class.java)

}