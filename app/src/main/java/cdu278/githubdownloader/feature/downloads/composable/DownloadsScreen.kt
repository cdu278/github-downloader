package cdu278.githubdownloader.feature.downloads.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cdu278.githubdownloader.feature.downloads.DownloadsViewModel

@Composable
fun DownloadsScreen(
    viewModel: DownloadsViewModel,
    modifier: Modifier = Modifier
) {
    Text("Downloads")
}