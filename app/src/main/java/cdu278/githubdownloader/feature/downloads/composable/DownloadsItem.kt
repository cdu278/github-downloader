package cdu278.githubdownloader.feature.downloads.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cdu278.githubdownloader.R
import cdu278.githubdownloader.feature.downloads.DownloadItemUi
import cdu278.githubdownloader.feature.downloads.DownloadItemUi.State.Cancelled
import cdu278.githubdownloader.feature.downloads.DownloadItemUi.State.Failed
import cdu278.githubdownloader.feature.downloads.DownloadItemUi.State.Finished
import cdu278.githubdownloader.feature.downloads.DownloadItemUi.State.Started
import cdu278.githubdownloader.feature.info.composable.RepoInfo
import cdu278.githubdownloader.ui.Margins
import cdu278.githubdownloader.ui.SecondaryContentAlpha

@Composable
fun DownloadsItem(
    item: DownloadItemUi,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = Margins.default, vertical = Margins.half)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            RepoInfo(
                title = item.title,
                description = item.description,
            )
            StateText(item.state)
        }
        if (item.state == Started) {
            Spacer(Modifier.width(Margins.default))
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Composable
private fun StateText(
    state: DownloadItemUi.State,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(
            id = when (state) {
                Started -> R.string.downloads_state_started
                Finished -> R.string.downloads_state_finished
                Failed -> R.string.downloads_state_failed
                Cancelled -> R.string.downloads_state_cancelled
            }
        ),
        fontSize = 12.sp,
        color = when (state) {
            Failed, Cancelled -> MaterialTheme.colorScheme.error
            else -> LocalContentColor.current.copy(alpha = SecondaryContentAlpha)
        },
        modifier = modifier
    )
}