package cdu278.githubdownloader.feature.search

data class SearchItemUi(
    val id: Long,
    val title: String,
    val description: String?,
    val downloadState: DownloadState,
) {

    enum class DownloadState {

        NotStarted,
        Started,
        Finished,
    }
}