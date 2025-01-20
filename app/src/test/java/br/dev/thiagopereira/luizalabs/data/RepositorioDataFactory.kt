package br.dev.thiagopereira.luizalabs.data

import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import kotlin.random.Random

class RepositorioDataFactory {

    companion object {
        fun create(id: Long = Random.nextLong()): RepositorioEntity {
            return RepositorioEntity(
                id = id,
                name = "Repository $id",
                fullName = "Full Name $id",
                description = "Description $id",
                stargazersCount = Random.nextInt(),
                forksCount = Random.nextInt(),
                openIssuesCount = Random.nextInt(),
                language = "language",
                owner = RepositorioEntity.Owner(
                    login = "Owner $id",
                    avatarUrl = "avatarUrl"
                )
            )
        }

        fun createMany(count: Int) = (1L..count).map { create(it) }
    }

}