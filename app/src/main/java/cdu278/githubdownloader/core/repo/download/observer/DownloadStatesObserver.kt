package cdu278.githubdownloader.core.repo.download.observer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Started
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.service.DownloadService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadStatesObserver @AssistedInject constructor(
    private val repository: RepoDownloadRepository,
    private val downloadService: DownloadService,
    @Assisted
    private val coroutineScope: CoroutineScope,
) {

    @AssistedFactory
    interface Factory {

        fun create(coroutineScope: CoroutineScope): DownloadStatesObserver
    }

    private var receiver: BroadcastReceiver? = null

    private fun createStateChangesReceiver(): BroadcastReceiver {
        return DownloadStateChangesReceiver(
            repository,
            downloadService,
            coroutineScope,
        ).also { receiver = it }
    }

    fun register(context: Context) {
        coroutineScope.launch {
            syncDownloadStates()
            ContextCompat.registerReceiver(
                context,
                createStateChangesReceiver(),
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                ContextCompat.RECEIVER_EXPORTED,
            )
        }
    }

    private suspend fun syncDownloadStates() {
        withContext(Dispatchers.Default) {
            repository
                .getDownloadIdsByState(Started)
                .map { downloadId ->
                    async {
                        Pair(downloadId, downloadService.state(downloadId))
                    }
                }
                .awaitAll()
                .filter { (_, actualState) -> actualState != Started }
                .map { (downloadId, actualState) ->
                    launch {
                        repository.updateState(downloadId, actualState)
                    }
                }
                .joinAll()
        }
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(receiver)
        receiver = null
    }
}