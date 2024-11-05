package cdu278.githubdownloader.core.repo.search.repository.remote

import kotlinx.serialization.Serializable

@Serializable
class SearchRepoResult(
    val items: List<RemoteRepo>,
)