package cdu278.githubdownloader.feature.downloads

data class DownloadItemUi(
    val id: Long,
    val title: String,
    val description: String?,
    val state: State,
) {

    enum class State {

        Started,
        Finished,
        Failed,
        Cancelled,
    }
}