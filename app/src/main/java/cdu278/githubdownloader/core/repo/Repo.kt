package cdu278.githubdownloader.core.repo

interface Repo {

    val id: Long

    val ownerLogin: String

    val name: String

    val description: String?

    val url: String
}