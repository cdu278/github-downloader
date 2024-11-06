package cdu278.githubdownloader.core.repo.download.urlFactory

import cdu278.githubdownloader.core.repo.Repo

interface RepoDownloadUrlFactory {

    fun create(repo: Repo): String
}