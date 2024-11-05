package cdu278.githubdownloader.core.repo.download.di

import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepository
import cdu278.githubdownloader.core.repo.download.repository.RepoDownloadRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DownloadModule {

    @Binds
    fun bindRepository(impl: RepoDownloadRepositoryImpl): RepoDownloadRepository
}