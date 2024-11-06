package cdu278.githubdownloader.core.repo.download.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cdu278.githubdownloader.core.repo.download.RepoDownloadState
import cdu278.githubdownloader.core.repo.download.db.entity.RepoDownloadEntity
import cdu278.githubdownloader.core.repo.download.db.entity.RepoIdAndDownloadState
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RepoDownloadDao {

    @Query("""
        SELECT id, state
        FROM repo_download
        WHERE id IN (:repoIds)
    """)
    abstract fun statesFlow(repoIds: List<Long>): Flow<List<RepoIdAndDownloadState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: RepoDownloadEntity)

    @Query("""
        UPDATE repo_download
        SET state = :newState
        WHERE download_id = :downloadId
    """)
    abstract suspend fun updateState(downloadId: Long, newState: RepoDownloadState)
}