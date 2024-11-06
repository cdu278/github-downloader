package cdu278.githubdownloader.core.repo.download.service

import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState

interface DownloadService {

    fun download(repo: Repo): Long

    suspend fun state(downloadId: Long): RepoDownloadState
}