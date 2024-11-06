package cdu278.githubdownloader.core.repo.download.usercase

import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.service.DownloadService
import javax.inject.Inject

class DownloadRepoUseCase @Inject constructor(
    private val repository: RepoDownloadRepository,
    private val downloadService: DownloadService,
) {

    suspend operator fun invoke(repo: Repo) {
        repository.create(
            repo,
            downloadId = downloadService.download(repo)
        )
    }
}