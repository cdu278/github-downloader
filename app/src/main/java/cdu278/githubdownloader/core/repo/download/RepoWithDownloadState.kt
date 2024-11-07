package cdu278.githubdownloader.core.repo.download

import cdu278.githubdownloader.core.repo.Repo

interface RepoWithDownloadState : Repo {

    val downloadState: RepoDownloadState
}
