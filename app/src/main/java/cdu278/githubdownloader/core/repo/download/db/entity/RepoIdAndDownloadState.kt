package cdu278.githubdownloader.core.repo.download.db.entity

import cdu278.githubdownloader.core.repo.download.RepoDownloadState

class RepoIdAndDownloadState(
    val id: Long,
    val state: RepoDownloadState,
)