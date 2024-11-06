package cdu278.githubdownloader.core.repo.download.di

import android.app.DownloadManager
import android.content.Context
import androidx.core.content.getSystemService
import androidx.room.Room
import cdu278.githubdownloader.core.repo.download.db.RepoDownloadDatabase
import cdu278.githubdownloader.core.repo.download.db.dao.RepoDownloadDao
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepositoryImpl
import cdu278.githubdownloader.core.repo.download.service.DownloadService
import cdu278.githubdownloader.core.repo.download.service.DownloadServiceImpl
import cdu278.githubdownloader.core.repo.download.urlFactory.RepoDownloadUrlFactory
import cdu278.githubdownloader.core.repo.download.urlFactory.RepoDownloadUrlFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepoDownloadModule {

    companion object {

        @Provides
        @Singleton
        fun provideDb(@ApplicationContext context: Context): RepoDownloadDatabase {
            return Room
                .databaseBuilder(context, RepoDownloadDatabase::class.java, "repo_downloads")
                .build()
        }

        @Provides
        fun provideDao(db: RepoDownloadDatabase): RepoDownloadDao = db.dao

        @Provides
        fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager {
            return context.getSystemService()!!
        }
    }

    @Binds
    fun bindRepository(impl: RepoDownloadRepositoryImpl): RepoDownloadRepository

    @Binds
    fun bindUrlFactory(impl: RepoDownloadUrlFactoryImpl): RepoDownloadUrlFactory

    @Binds
    fun bindService(impl: DownloadServiceImpl): DownloadService
}