package br.dev.thiagopereira.luizalabs.di

import br.dev.thiagopereira.luizalabs.remote.GitHubService
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteModule::class]
)
object TestRemoteModule {

    @Provides
    @Singleton
    fun provideGitHubService() = Mockito.mock(GitHubService::class.java)

}