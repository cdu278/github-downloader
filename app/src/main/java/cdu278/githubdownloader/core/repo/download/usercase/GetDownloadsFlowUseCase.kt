package cdu278.githubdownloader.core.repo.download.usercase

import cdu278.githubdownloader.core.repo.download.RepoWithDownloadState
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadsFlowUseCase @Inject constructor(
    private val repository: RepoDownloadRepository,
) {

    operator fun invoke(): Flow<List<RepoWithDownloadState>> = repository.flow
}