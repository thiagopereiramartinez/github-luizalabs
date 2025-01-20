package br.dev.thiagopereira.luizalabs.ui.pullrequests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.dev.thiagopereira.luizalabs.R
import br.dev.thiagopereira.luizalabs.db.model.PullRequestEntity
import br.dev.thiagopereira.luizalabs.ui.pullrequests.preview.PullRequestListItemPreviewParameterProvider
import br.dev.thiagopereira.luizalabs.ui.theme.GitHubLuizaLabsTheme
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun PullRequestListItem(
    modifier: Modifier = Modifier,
    pullRequest: PullRequestEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().testTag("prId:${pullRequest.id}"),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = pullRequest.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.testTag("title")
            )
            pullRequest.body?.let { body ->
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    modifier = Modifier.testTag("body")
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .testTag("avatar"),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pullRequest.user.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.owner_avatar),
                    placeholder = painterResource(R.drawable.ic_owner_placeholder),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = pullRequest.user.login,
                    modifier = Modifier.testTag("owner")
                )
            }
        }
    }
}

@Preview
@Composable
private fun PullRequestListItem_Preview(
    @PreviewParameter(PullRequestListItemPreviewParameterProvider::class) pullRequest: PullRequestEntity
) {
    GitHubLuizaLabsTheme {
        PullRequestListItem(
            pullRequest = pullRequest
        ) { }
    }
}
