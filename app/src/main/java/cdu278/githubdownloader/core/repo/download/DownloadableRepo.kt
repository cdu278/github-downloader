package cdu278.githubdownloader.core.repo.download

import cdu278.githubdownloader.core.repo.Repo

interface DownloadableRepo : Repo {

    val downloadState: RepoDownloadState
}

class RepoWithDownloadState(
    repo: Repo,
    override val downloadState: RepoDownloadState,
) : DownloadableRepo,
    Repo by repo