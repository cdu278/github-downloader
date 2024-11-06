package cdu278.githubdownloader.core.repo.download.urlFactory

import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.di.GithubApiBaseUrl
import javax.inject.Inject

class RepoDownloadUrlFactoryImpl @Inject constructor(
    @GithubApiBaseUrl
    private val baseUrl: String,
) : RepoDownloadUrlFactory {

    override fun create(repo: Repo): String {
        return with(repo) { "$baseUrl/repos/$ownerLogin/$name/zipball" }
    }
}