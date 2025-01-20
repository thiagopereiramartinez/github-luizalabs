package br.dev.thiagopereira.luizalabs.ui.repositorios.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity

class RepositorioListItemPreviewParameterProvider : PreviewParameterProvider<RepositorioEntity> {

    override val values: Sequence<RepositorioEntity>
        get() = sequenceOf(
            RepositorioEntity(
                id = 1,
                name = "Nome do Repositório",
                fullName = "Nome do Repositório",
                description = "Descrição do Repositório",
                owner = RepositorioEntity.Owner(
                    login = "Owner Teste",
                    avatarUrl = "https://placehold.co/400"
                ),
                stargazersCount = 10,
                forksCount = 5,
                language = "Kotlin",
                openIssuesCount = 10
            )
        )
}