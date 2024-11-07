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
interface RepoDownloadDao {

    @get:Query("""
        SELECT *
        FROM repo_download
        ORDER BY created_at DESC
    """)
    val flow: Flow<List<RepoDownloadEntity>>

    @Query("""
        SELECT id, state
        FROM repo_download
        WHERE id IN (:repoIds)
    """)
    fun statesFlow(repoIds: List<Long>): Flow<List<RepoIdAndDownloadState>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RepoDownloadEntity)

    @Query("""
        UPDATE repo_download
        SET state = :newState
        WHERE download_id = :downloadId
    """)
    suspend fun updateState(downloadId: Long, newState: RepoDownloadState)

    @Query("""
        SELECT download_id
        FROM repo_download
        WHERE state = :state
    """)
    suspend fun selectDownloadIdsByState(state: RepoDownloadState): List<Long>
}