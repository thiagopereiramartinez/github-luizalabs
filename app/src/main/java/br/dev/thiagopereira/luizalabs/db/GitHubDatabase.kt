package br.dev.thiagopereira.luizalabs.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.dev.thiagopereira.luizalabs.db.dao.LastUpdatedDao
import br.dev.thiagopereira.luizalabs.db.dao.PullRequestDao
import br.dev.thiagopereira.luizalabs.db.dao.RemoteKeyDao
import br.dev.thiagopereira.luizalabs.db.dao.RepositorioDao
import br.dev.thiagopereira.luizalabs.db.model.LastUpdatedEntity
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.db.model.RemoteKeyEntity
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity

@Database(
    entities = [
        LastUpdatedEntity::class,
        PullRequestEntity::class,
        RepositorioEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1
)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun lastUpdatedDao(): LastUpdatedDao
    abstract fun pullRequestDao(): PullRequestDao
    abstract fun repositorioDao(): RepositorioDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}