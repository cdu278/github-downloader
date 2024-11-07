package cdu278.githubdownloader.core.repo.download.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cdu278.githubdownloader.common.typeconverter.InstantConverter
import cdu278.githubdownloader.core.repo.download.db.dao.RepoDownloadDao
import cdu278.githubdownloader.core.repo.download.db.entity.RepoDownloadEntity
import cdu278.githubdownloader.core.repo.download.db.typeconverter.RepoDownloadStateConverter

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        RepoDownloadEntity::class,
    ]
)
@TypeConverters(
    InstantConverter::class,
    RepoDownloadStateConverter::class,
)
abstract class RepoDownloadDatabase : RoomDatabase() {

    abstract val dao: RepoDownloadDao
}