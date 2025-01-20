package br.dev.thiagopereira.luizalabs.data

import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import kotlin.random.Random

class PullRequestDataFactory {

    companion object {
        fun create(id: Long = Random.nextLong(), repositioId: Long = Random.nextLong()): PullRequestEntity {
            return PullRequestEntity(
                id = id,
                title = "Pull Request $id",
                body = "Body $id",
                user = PullRequestEntity.User(
                    login = "User $id",
                    avatarUrl = "avatar"
                ),
                links = PullRequestEntity.Links(
                    html = PullRequestEntity.Links.Link(
                        href = "https://github.com"
                    )
                ),
                createdAt = "2023-01-01",
                updatedAt = "2023-01-01",
                repositorioId = repositioId
            )
        }

        fun createMany(count: Int) = (1L..count).map { create(it) }
    }

}