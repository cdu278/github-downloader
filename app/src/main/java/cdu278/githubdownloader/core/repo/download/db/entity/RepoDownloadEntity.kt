package cdu278.githubdownloader.core.repo.download.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import cdu278.githubdownloader.core.repo.download.RepoWithDownloadState
import kotlinx.datetime.Instant

@Entity(
    tableName = "repo_download",
    indices = [
        Index("download_id", unique = true),
        Index("created_at"),
        Index("state"),
    ],
)
class RepoDownloadEntity(
    @PrimaryKey
    override val id: Long,
    @ColumnInfo("owner")
    override val ownerLogin: String,
    override val name: String,
    override val description: String?,
    override val url: String,
    @ColumnInfo(name = "download_id")
    val downloadId: Long,
    @ColumnInfo(name = "state")
    override val downloadState: RepoDownloadState,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
) : RepoWithDownloadState {

    companion object {

        fun new(repo: Repo, downloadId: Long, now: Instant): RepoDownloadEntity {
            return with(repo) {
                RepoDownloadEntity(
                    id = id,
                    ownerLogin = ownerLogin,
                    name = name,
                    description = description,
                    url = url,
                    downloadId = downloadId,
                    downloadState = RepoDownloadState.Started,
                    createdAt = now,
                )
            }
        }
    }
}