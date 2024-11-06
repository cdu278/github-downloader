package cdu278.githubdownloader.core.repo.download.usercase

import cdu278.githubdownloader.core.repo.download.DownloadableRepo
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadsFlowUseCase @Inject constructor(
    private val repository: RepoDownloadRepository,
) {

    operator fun invoke(): Flow<List<DownloadableRepo>> = repository.flow
}