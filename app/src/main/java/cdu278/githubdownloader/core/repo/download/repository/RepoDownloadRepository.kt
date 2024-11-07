package cdu278.githubdownloader.core.repo.download.repository

import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import cdu278.githubdownloader.core.repo.download.RepoWithDownloadState
import kotlinx.coroutines.flow.Flow

interface RepoDownloadRepository {

    val flow: Flow<List<RepoWithDownloadState>>

    fun statesFlow(repoIds: List<Long>): Flow<Map<Long, RepoDownloadState>>

    suspend fun create(repo: Repo, downloadId: Long)

    suspend fun updateState(downloadId: Long, newState: RepoDownloadState)

    suspend fun getDownloadIdsByState(state: RepoDownloadState): List<Long>
}