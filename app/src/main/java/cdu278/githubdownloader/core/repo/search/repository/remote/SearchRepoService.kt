package cdu278.githubdownloader.core.repo.search.repository.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRepoService {

    @GET("search/repositories")
    suspend fun call(@Query("q") query: String): Response<SearchRepoResult>
}