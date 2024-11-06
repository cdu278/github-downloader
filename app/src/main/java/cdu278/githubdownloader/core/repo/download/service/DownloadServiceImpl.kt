package cdu278.githubdownloader.core.repo.download.service

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import cdu278.githubdownloader.core.repo.download.urlFactory.RepoDownloadUrlFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadServiceImpl @Inject constructor(
    private val downloadManager: DownloadManager,
    private val urlFactory: RepoDownloadUrlFactory,
) : DownloadService {

    override fun download(repo: Repo): Long {
        val zipUri = Uri.parse(urlFactory.create(repo))
        return downloadManager.enqueue(
            DownloadManager.Request(zipUri)
                .setTitle(with(repo) { "$ownerLogin/$name" })
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    with(repo) { "${ownerLogin}_$name.zip" }
                )
        )
    }

    override suspend fun state(downloadId: Long): RepoDownloadState {
        return withContext(Dispatchers.IO) {
            val cursor =
                downloadManager
                    .query(
                        DownloadManager.Query()
                            .apply { setFilterById(downloadId) }
                    )
                    .takeIf { it.count > 0 }
                    ?: return@withContext RepoDownloadState.NotStarted
            cursor.moveToFirst()
            val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            when (cursor.getInt(statusColumnIndex)) {
                DownloadManager.STATUS_SUCCESSFUL -> RepoDownloadState.Finished
                DownloadManager.STATUS_FAILED -> RepoDownloadState.Failed
                else -> RepoDownloadState.Started
            }
        }
    }
}