package cdu278.githubdownloader.core.repo.download

import cdu278.githubdownloader.core.repo.Repo

fun RepoWithDownloadState(
    repo: Repo,
    downloadState: RepoDownloadState,
): RepoWithDownloadState {
    return RepoWithDownloadStateImpl(
        repo,
        downloadState,
    )
}

private class RepoWithDownloadStateImpl(
    repo: Repo,
    override val downloadState: RepoDownloadState,
) : RepoWithDownloadState,
    Repo by repo
