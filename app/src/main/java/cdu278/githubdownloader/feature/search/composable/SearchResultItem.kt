package cdu278.githubdownloader.feature.search.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cdu278.githubdownloader.R
import cdu278.githubdownloader.feature.description.composable.RepoDescription
import cdu278.githubdownloader.feature.search.SearchItemUi
import cdu278.githubdownloader.feature.search.SearchItemUi.DownloadState.Finished
import cdu278.githubdownloader.feature.search.SearchItemUi.DownloadState.NotStarted
import cdu278.githubdownloader.feature.search.SearchItemUi.DownloadState.Started
import cdu278.githubdownloader.ui.Margins

@Composable
fun SearchResultItem(
    item: SearchItemUi,
    download: (Long) -> Unit,
    view: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = Margins.default, vertical = Margins.half)
    ) {
        Info(
            item,
            modifier = Modifier
                .weight(1f)
        )
        Spacer(Modifier.width(Margins.default))
        DownloadState(
            item.downloadState,
            download = { download(item.id) },
        )
        ViewButton(
            onClick = { view(item.id) },
        )
    }
}

@Composable
private fun Info(
    item: SearchItemUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(item.title)
        item.description?.let {
            RepoDescription(text = it)
        }
    }
}

@Composable
private fun DownloadState(
    downloadState: SearchItemUi.DownloadState,
    download: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when (downloadState) {
            NotStarted ->
                IconButton(onClick = download) {
                    Icon(painterResource(R.drawable.ic_download), contentDescription = null)
                }
            Started -> CircularProgressIndicator()
            Finished -> Icon(painterResource(R.drawable.ic_downloaded), contentDescription = null)
        }
    }
}

@Composable
private fun ViewButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick,
        modifier = modifier
    ) {
        Icon(painterResource(R.drawable.ic_open), contentDescription = null)
    }
}