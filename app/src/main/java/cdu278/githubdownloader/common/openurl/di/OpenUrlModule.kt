package cdu278.githubdownloader.common.openurl.di

import cdu278.githubdownloader.common.openurl.service.OpenUrlService
import cdu278.githubdownloader.common.openurl.service.OpenUrlServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OpenUrlModule {

    @Binds
    fun bindService(impl: OpenUrlServiceImpl): OpenUrlService
}