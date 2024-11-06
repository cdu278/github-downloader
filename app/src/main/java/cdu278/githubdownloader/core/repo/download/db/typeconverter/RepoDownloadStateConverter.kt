package cdu278.githubdownloader.core.repo.download.db.typeconverter

import androidx.room.TypeConverter
import cdu278.githubdownloader.core.repo.download.RepoDownloadState

class RepoDownloadStateConverter {

    @TypeConverter
    fun toName(state: RepoDownloadState): String = state.name

    @TypeConverter
    fun fromName(name: String) = RepoDownloadState.valueOf(name)
}