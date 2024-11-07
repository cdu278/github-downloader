package cdu278.githubdownloader.core.repo.download.observer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.service.DownloadRepoService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DownloadStateChangesReceiver @AssistedInject constructor(
    private val repository: RepoDownloadRepository,
    private val downloadService: DownloadRepoService,
    @Assisted
    private val coroutineScope: CoroutineScope,
) : BroadcastReceiver() {

    @AssistedFactory
    interface Factory {

        fun create(coroutineScope: CoroutineScope): DownloadStateChangesReceiver
    }

    override fun onReceive(context: Context, intent: Intent) {
        coroutineScope.launch {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            repository.updateState(
                downloadId,
                newState = downloadService.state(downloadId)
            )
        }
    }
}