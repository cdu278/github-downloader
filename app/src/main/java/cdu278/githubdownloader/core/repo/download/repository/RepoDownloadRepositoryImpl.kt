package cdu278.githubdownloader.core.repo.download.repository

import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RepoDownloadRepositoryImpl @Inject constructor(
) : RepoDownloadRepository {

    override fun statesFlow(repoIds: List<Long>): Flow<Map<Long, RepoDownloadState>> {
        return flowOf(emptyMap())
    }
}