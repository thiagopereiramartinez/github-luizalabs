package br.dev.thiagopereira.luizalabs.ui.pullrequests.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity

class PullRequestListItemPreviewParameterProvider : PreviewParameterProvider<PullRequestEntity> {

    override val values: Sequence<PullRequestEntity>
        get() = sequenceOf(
            PullRequestEntity(
                id = 1,
                repositorioId = 1,
                title = "Return same value as same Index start and end on `replaceRange`",
                body = "This is replaceRange but, It does insert.\\r\\nWhen set the same Index on start and end, it have done append CharSequence currently. But it should be nothing changed.\\r\\n\\r\\nhttps://pl.kotl.in/3jc8tMz2B\\r\\n\\r\\n```kotlin\\r\\nfun main() {\\r\\n    val origin = \\\"123\\\"\\r\\n    val replaced = origin.replaceRange(startIndex = 1, endIndex = 1, replacement = \\\"9\\\")\\r\\n\\t\\r\\n    println(replaced)\\r\\n    // 1923\\r\\n}\\r\\n```",
                user = PullRequestEntity.User(
                    login = "User Teste",
                    avatarUrl = "https://placehold.co/400"
                ),
                createdAt = "2021-03-12T02:27:26Z",
                updatedAt = "2021-03-12T02:27:26Z",
                links = PullRequestEntity.Links(
                    html = PullRequestEntity.Links.Link(
                        href = ""
                    )
                )
            )
        )
}