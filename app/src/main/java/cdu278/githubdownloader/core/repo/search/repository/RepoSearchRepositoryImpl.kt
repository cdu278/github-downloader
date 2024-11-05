package cdu278.githubdownloader.core.repo.search.repository

import cdu278.githubdownloader.core.ApiError
import cdu278.githubdownloader.core.Result
import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.search.repository.remote.SearchRepoService
import java.io.IOException
import javax.inject.Inject

class RepoSearchRepositoryImpl @Inject constructor(
    private val searchService: SearchRepoService,
) : RepoSearchRepository {

    override suspend fun search(username: String): Result<List<Repo>, ApiError> {
        val response = try {
            searchService.call(query = "user:$username")
        } catch (e: IOException) {
            return Result.Failure(ApiError.Io)
        }
        return if (response.isSuccessful) {
            Result.Ok(response.body()!!.items)
        } else {
            when (response.code()) {
                422 -> Result.Ok(emptyList())
                429, 403 -> Result.Failure(ApiError.TooManyRequests)
                else -> Result.Failure(ApiError.Unknown)
            }
        }
    }
}