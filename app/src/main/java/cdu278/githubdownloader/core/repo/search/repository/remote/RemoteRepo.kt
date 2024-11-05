package cdu278.githubdownloader.core.repo.search.repository.remote

import cdu278.githubdownloader.core.repo.Repo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RemoteRepo(
    override val id: Long,
    override val name: String,
    override val description: String?,
    @SerialName("html_url")
    override val url: String,
    private val owner: Owner,
) : Repo {

    override val ownerLogin: String
        get() = owner.login

    @Serializable
    class Owner(
        val login: String,
    )
}