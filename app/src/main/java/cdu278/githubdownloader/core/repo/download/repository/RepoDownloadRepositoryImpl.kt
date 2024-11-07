package cdu278.githubdownloader.core.repo.download.repository

import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import cdu278.githubdownloader.core.repo.download.RepoWithDownloadState
import cdu278.githubdownloader.core.repo.download.db.dao.RepoDownloadDao
import cdu278.githubdownloader.core.repo.download.db.entity.RepoDownloadEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import javax.inject.Inject

class RepoDownloadRepositoryImpl @Inject constructor(
    private val dao: RepoDownloadDao,
    private val clock: Clock,
) : RepoDownloadRepository {

    override val flow: Flow<List<RepoWithDownloadState>>
        get() = dao.flow

    override fun statesFlow(repoIds: List<Long>): Flow<Map<Long, RepoDownloadState>> {
        return dao.statesFlow(repoIds).map { states ->
            withContext(Dispatchers.Default) {
                states.associate { it.id to it.state }
            }
        }
    }

    override suspend fun create(repo: Repo, downloadId: Long) {
        dao.insert(RepoDownloadEntity.new(repo, downloadId, clock.now()))
    }

    override suspend fun updateState(downloadId: Long, newState: RepoDownloadState) {
        dao.updateState(downloadId, newState)
    }

    override suspend fun getDownloadIdsByState(state: RepoDownloadState): List<Long> {
        return dao.selectDownloadIdsByState(state)
    }
}