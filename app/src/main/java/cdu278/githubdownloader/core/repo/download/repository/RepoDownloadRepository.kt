package cdu278.githubdownloader.core.repo.download.repository

import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import kotlinx.coroutines.flow.Flow

interface RepoDownloadRepository {

    fun statesFlow(repoIds: List<Long>): Flow<Map<Long, RepoDownloadState>>
}