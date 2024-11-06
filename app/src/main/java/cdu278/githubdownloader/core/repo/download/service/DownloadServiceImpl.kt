package cdu278.githubdownloader.core.repo.download.service

import android.app.DownloadManager
import android.app.DownloadManager.COLUMN_STATUS
import android.app.DownloadManager.STATUS_FAILED
import android.app.DownloadManager.STATUS_SUCCESSFUL
import android.net.Uri
import android.os.Environment
import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Cancelled
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Failed
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Finished
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Started
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
                    ?: return@withContext Cancelled
            cursor.moveToFirst()
            val statusColumnIndex = cursor.getColumnIndex(COLUMN_STATUS)
            when (cursor.getInt(statusColumnIndex)) {
                STATUS_SUCCESSFUL -> Finished
                STATUS_FAILED -> Failed
                else -> Started
            }
        }
    }
}