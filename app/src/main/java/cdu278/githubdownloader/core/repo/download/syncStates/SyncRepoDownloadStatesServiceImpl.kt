package cdu278.githubdownloader.core.repo.download.syncStates

import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Started
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.service.DownloadRepoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncRepoDownloadStatesServiceImpl @Inject constructor(
    private val repository: RepoDownloadRepository,
    private val downloadService: DownloadRepoService,
) : SyncRepoDownloadStatesService {

    override suspend fun sync() {
        withContext(Dispatchers.Default) {
            repository
                .getDownloadIdsByState(Started)
                .map { downloadId ->
                    async {
                        Pair(downloadId, downloadService.state(downloadId))
                    }
                }
                .awaitAll()
                .filter { (_, actualState) -> actualState != Started }
                .map { (downloadId, actualState) ->
                    launch {
                        repository.updateState(downloadId, actualState)
                    }
                }
                .joinAll()
        }
    }
}