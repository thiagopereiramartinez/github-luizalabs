package br.dev.thiagopereira.luizalabs.ui.repositorios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.dev.thiagopereira.luizalabs.R
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import br.dev.thiagopereira.luizalabs.ui.components.ItemCount
import br.dev.thiagopereira.luizalabs.ui.repositorios.preview.RepositorioListItemPreviewParameterProvider
import br.dev.thiagopereira.luizalabs.ui.theme.GitHubLuizaLabsTheme
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun RepositorioListItem(
    modifier: Modifier = Modifier,
    repositorio: RepositorioEntity,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().testTag("repositorioId:${repositorio.id}")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = repositorio.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.testTag("name")
                )
                repositorio.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        modifier = Modifier.testTag("description")
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ItemCount(
                        modifier = Modifier.testTag("forks"),
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_fork),
                                contentDescription = null
                            )
                        },
                        count = repositorio.forksCount
                    )
                    ItemCount(
                        modifier = Modifier.testTag("stars"),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                        },
                        count = repositorio.stargazersCount
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .testTag("avatar"),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(repositorio.owner.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.owner_avatar),
                    placeholder = painterResource(R.drawable.ic_owner_placeholder),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = repositorio.owner.login,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("owner")
                )
            }
        }
    }
}

@Preview
@Composable
private fun RepositorioListItem_Preview(
    @PreviewParameter(RepositorioListItemPreviewParameterProvider::class) repositorio: RepositorioEntity
) {
    GitHubLuizaLabsTheme {
        RepositorioListItem(
            repositorio = repositorio,
            onClick = { }
        )
    }
}
