package cdu278.githubdownloader.core.repo.download.observer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import cdu278.githubdownloader.core.repo.download.syncStates.SyncRepoDownloadStatesService
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

    private var receiver: BroadcastReceiver? = null

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    suspend fun observe(context: Context) {
        ContextCompat.registerReceiver(
            context,
            receiverFactory
                .create(coroutineScope)
                .also { receiver = it },
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED,
        )
        while (true) {
            syncStatesService.sync()
            delay(5.seconds)
        }
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(receiver)
        receiver = null
        coroutineScope.coroutineContext.cancelChildren()
    }
}