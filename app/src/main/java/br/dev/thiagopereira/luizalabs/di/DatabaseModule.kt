package br.dev.thiagopereira.luizalabs.di

import android.content.Context
import androidx.room.Room
import br.dev.thiagopereira.luizalabs.db.GitHubDatabase
import br.dev.thiagopereira.luizalabs.db.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): GitHubDatabase =
        Room.databaseBuilder(
            appContext,
            GitHubDatabase::class.java,
            "github.db"
        ).build()

    @Provides
    fun providePullRequestDao(database: GitHubDatabase) = database.pullRequestDao()

    @Provides
    fun provideRepositorioDao(database: GitHubDatabase) = database.repositorioDao()

    @Provides
    fun provideRemoteKeyDao(database: GitHubDatabase): RemoteKeyDao = database.remoteKeyDao()

    @Provides
    fun provideLastUpdatedDao(database: GitHubDatabase) = database.lastUpdatedDao()

}