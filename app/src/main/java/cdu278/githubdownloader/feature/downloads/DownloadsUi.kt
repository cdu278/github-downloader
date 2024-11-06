package cdu278.githubdownloader.feature.downloads

import kotlinx.collections.immutable.ImmutableList

sealed interface DownloadsUi {

    data object Initial : DownloadsUi

    data class Loaded(val items: ImmutableList<DownloadItemUi>) : DownloadsUi
}