package cdu278.githubdownloader.core.repo.search.repository

import cdu278.githubdownloader.common.Result
import cdu278.githubdownloader.core.ApiError
import cdu278.githubdownloader.core.repo.Repo

interface RepoSearchRepository {

    suspend fun search(username: String): Result<List<Repo>, ApiError>
}