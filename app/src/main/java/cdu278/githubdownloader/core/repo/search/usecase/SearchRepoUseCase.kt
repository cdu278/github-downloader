package cdu278.githubdownloader.core.repo.search.usecase

import cdu278.githubdownloader.common.Result
import cdu278.githubdownloader.common.map
import cdu278.githubdownloader.core.ApiError
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.NotStarted
import cdu278.githubdownloader.core.repo.download.RepoWithDownloadState
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.search.repository.RepoSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepoUseCase @Inject constructor(
    private val searchRepository: RepoSearchRepository,
    private val downloadRepository: RepoDownloadRepository,
) {

    suspend operator fun invoke(username: String): Result<Flow<List<RepoWithDownloadState>>, ApiError> {
        return searchRepository.search(username).map { repos ->
            val repoIds = withContext(Dispatchers.Default) {
                repos.map { it.id }
            }
            downloadRepository
                .statesFlow(repoIds)
                .map { downloadStateMap ->
                    withContext(Dispatchers.Default) {
                        repos.map { repo ->
                            RepoWithDownloadState(
                                repo,
                                downloadState = downloadStateMap[repo.id] ?: NotStarted,
                            )
                        }
                    }
                }
        }
    }
}