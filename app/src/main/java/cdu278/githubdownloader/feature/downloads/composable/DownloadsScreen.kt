package cdu278.githubdownloader.feature.downloads.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cdu278.githubdownloader.R
import cdu278.githubdownloader.feature.downloads.DownloadItemUi
import cdu278.githubdownloader.feature.downloads.DownloadsUi.Initial
import cdu278.githubdownloader.feature.downloads.DownloadsUi.Loaded
import cdu278.githubdownloader.feature.downloads.DownloadsViewModel

@Composable
fun DownloadsScreen(
    viewModel: DownloadsViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val ui by viewModel.uiFlow.collectAsStateWithLifecycle()
        when (val u = ui) {
            is Initial -> {}
            is Loaded ->
                if (u.items.isEmpty()) {
                    Text(
                        text = stringResource(R.string.downloads_empty),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 70.dp)
                    )
                } else {
                    LazyColumn {
                        items(u.items, key = DownloadItemUi::id) {
                            DownloadsItem(item = it)
                        }
                    }
                }
        }
    }
}
