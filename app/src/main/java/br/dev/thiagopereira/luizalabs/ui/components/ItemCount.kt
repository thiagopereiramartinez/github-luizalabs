package br.dev.thiagopereira.luizalabs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.thiagopereira.luizalabs.ui.theme.GitHubLuizaLabsTheme
import br.dev.thiagopereira.luizalabs.ui.theme.Orange

@Composable
fun ItemCount(
    icon: @Composable () -> Unit,
    count: Int,
    contentColor: Color = Orange,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                propagateMinConstraints = true
            ) {
                icon()
            }
            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                Text(
                    text = count.toString()
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemCount_Preview() {
    GitHubLuizaLabsTheme {
        ItemCount(
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            },
            count = 10
        )
    }
}