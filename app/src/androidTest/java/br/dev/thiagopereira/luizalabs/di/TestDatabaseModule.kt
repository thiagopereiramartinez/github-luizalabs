package br.dev.thiagopereira.luizalabs.di

import android.content.Context
import androidx.room.Room
import br.dev.thiagopereira.luizalabs.db.GitHubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room
        .inMemoryDatabaseBuilder(
            context,
            GitHubDatabase::class.java
        )
        .setTransactionExecutor(Executors.newSingleThreadExecutor())
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun providePullRequestDao(database: GitHubDatabase) = database.pullRequestDao()

    @Provides
    @Singleton
    fun provideRepositorioDao(database: GitHubDatabase) = database.repositorioDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(database: GitHubDatabase) = database.remoteKeyDao()

    @Provides
    @Singleton
    fun provideLastUpdatedDao(database: GitHubDatabase) = database.lastUpdatedDao()


}