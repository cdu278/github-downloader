package cdu278.githubdownloader.core.repo.search.di

import cdu278.githubdownloader.core.repo.search.repository.RepoSearchRepository
import cdu278.githubdownloader.core.repo.search.repository.RepoSearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    @Provides
    fun provideRepository(retrofit: Retrofit): RepoSearchRepository {
        return RepoSearchRepositoryImpl(
            searchService = retrofit.create(),
        )
    }
}