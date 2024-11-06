package cdu278.githubdownloader.feature.info.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import cdu278.githubdownloader.ui.SecondaryContentAlpha

@Composable
fun RepoInfo(
    title: String,
    description: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (description != null) {
            RepoDescription(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RepoDescription(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        fontSize = 12.sp,
        color = LocalContentColor.current.copy(alpha = SecondaryContentAlpha),
        maxLines = 2,
        lineHeight = 14.sp,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}
