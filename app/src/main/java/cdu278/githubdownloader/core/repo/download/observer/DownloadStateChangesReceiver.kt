package cdu278.githubdownloader.core.repo.download.observer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.service.DownloadService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DownloadStateChangesReceiver(
    private val repository: RepoDownloadRepository,
    private val downloadService: DownloadService,
    private val coroutineScope: CoroutineScope,
) : BroadcastReceiver() {

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