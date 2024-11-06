package cdu278.githubdownloader.feature.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.githubdownloader.common.flow.UiSharingStarted
import cdu278.githubdownloader.core.repo.download.DownloadableRepo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Failed
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Finished
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.NotStarted
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Started
import cdu278.githubdownloader.core.repo.download.usercase.GetDownloadsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    getDownloadsFlow: GetDownloadsFlowUseCase,
) : ViewModel() {

    val uiFlow: StateFlow<DownloadsUi> =
        getDownloadsFlow().map { repos ->
            DownloadsUi.Loaded(
                items = withContext(Dispatchers.Default) {
                    repos
                        .map { it.asItemUi() }
                        .toImmutableList()
                }
            )
        }.stateIn(viewModelScope, UiSharingStarted, initialValue = DownloadsUi.Initial)

    private fun DownloadableRepo.asItemUi(): DownloadItemUi {
        return DownloadItemUi(
            id = id,
            title = "$ownerLogin/$name",
            description = description,
            state = when (downloadState) {
                Started, NotStarted -> DownloadItemUi.State.Started
                Finished -> DownloadItemUi.State.Finished
                Failed -> DownloadItemUi.State.Failed
            },
        )
    }
}