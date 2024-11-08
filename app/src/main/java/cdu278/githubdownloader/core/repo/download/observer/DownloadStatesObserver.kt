package cdu278.githubdownloader.core.repo.download.observer

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import cdu278.githubdownloader.core.repo.download.syncStates.SyncRepoDownloadStatesService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class DownloadStatesObserver @Inject constructor(
    private val receiverFactory: DownloadStateChangesReceiver.Factory,
    private val syncStatesService: SyncRepoDownloadStatesService,
) {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    suspend fun observe(context: Context) {
        val receiver = receiverFactory.create(coroutineScope)
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED,
        )
        try {
            syncStatesPeriodically()
        } catch (e: CancellationException) {
            context.unregisterReceiver(receiver)
            coroutineScope.coroutineContext.cancelChildren()
        }
    }

    private suspend fun syncStatesPeriodically() {
        while (true) {
            syncStatesService.sync()
            delay(5.seconds)
        }
    }
}