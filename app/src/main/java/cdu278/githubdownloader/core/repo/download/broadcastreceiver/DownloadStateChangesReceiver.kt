package cdu278.githubdownloader.core.repo.download.broadcastreceiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.service.DownloadService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DownloadStateChangesReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: RepoDownloadRepository

    @Inject
    lateinit var downloadService: DownloadService

    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Job()).launch {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            repository.updateState(
                downloadId,
                newState = downloadService.state(downloadId)
            )
        }
    }
}