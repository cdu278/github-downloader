package cdu278.githubdownloader.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @GithubApiBaseUrl
    fun provideApiBaseUrl(): String = "https://api.github.com"

    @Provides
    @Singleton
    fun provideRetrofit(@GithubApiBaseUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .callTimeout(2, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .baseUrl(baseUrl)
            .addConverterFactory(
                @Suppress("JSON_FORMAT_REDUNDANT")
                Json { ignoreUnknownKeys = true }
                    .asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    fun provideClock(): Clock = Clock.System
}