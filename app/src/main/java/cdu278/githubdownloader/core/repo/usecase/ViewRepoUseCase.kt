package cdu278.githubdownloader.core.repo.usecase

import cdu278.githubdownloader.common.openurl.service.OpenUrlService
import cdu278.githubdownloader.core.repo.Repo
import javax.inject.Inject

class ViewRepoUseCase @Inject constructor(
    private val service: OpenUrlService,
) {

    operator fun invoke(repo: Repo) {
        service.view(repo.url)
    }
}