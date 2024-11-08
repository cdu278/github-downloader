package cdu278.githubdownloader.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.githubdownloader.common.Result.Failure
import cdu278.githubdownloader.common.Result.Ok
import cdu278.githubdownloader.common.flow.UiSharingStarted
import cdu278.githubdownloader.common.flow.inputStateFlow
import cdu278.githubdownloader.core.ApiError
import cdu278.githubdownloader.core.ApiError.Io
import cdu278.githubdownloader.core.ApiError.TooManyRequests
import cdu278.githubdownloader.core.ApiError.Unknown
import cdu278.githubdownloader.core.repo.Repo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Cancelled
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Failed
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Finished
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.NotStarted
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Started
import cdu278.githubdownloader.core.repo.download.RepoWithDownloadState
import cdu278.githubdownloader.core.repo.download.usercase.DownloadRepoUseCase
import cdu278.githubdownloader.core.repo.search.usecase.SearchRepoUseCase
import cdu278.githubdownloader.core.repo.usecase.ViewRepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import cdu278.githubdownloader.feature.search.SearchItemUi.DownloadState as ItemDownloadState
import cdu278.githubdownloader.feature.search.SearchUi.State as UiState
import cdu278.githubdownloader.feature.search.SearchUi.State.Failed.Error as UiError

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val search: SearchRepoUseCase,
    private val download: DownloadRepoUseCase,
    private val view: ViewRepoUseCase,
) : ViewModel() {

    private val _usernameFlow =
        inputStateFlow(
            savedStateHandle,
            key = "username",
            initialValue = { "" }
        )
    val usernameFlow: StateFlow<String>
        get() = _usernameFlow

    fun inputUsername(value: String) {
        _usernameFlow.value = value
    }

    private val uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)

    private var updatingUiState: Job? = null

    private var foundRepos: List<Repo> = emptyList()

    fun search() {
        updatingUiState?.cancel()
        foundRepos = emptyList()
        updatingUiState = viewModelScope.launch {
            uiStateFlow.value = UiState.Loading
            when (val result = search(usernameFlow.value.trim())) {
                is Ok -> {
                    val reposFlow = result.value
                    reposFlow.collect { repos ->
                        foundRepos = repos
                        val items = withContext(Dispatchers.Default) {
                            repos
                                .map { it.asItemUi() }
                                .toImmutableList()

                        }
                        uiStateFlow.value = UiState.Loaded(items)
                    }
                }

                is Failure -> uiStateFlow.value = UiState.Failed(result.error.asUiError())
            }
        }
    }

    val uiFlow: StateFlow<SearchUi> =
        combine(
            usernameFlow,
            uiStateFlow,
        ) { username, state ->
            SearchUi(
                canSearch = username.isSingleWord(),
                state,
            )
        }.stateIn(viewModelScope, UiSharingStarted, initialValue = SearchUi())

    private suspend fun String.isSingleWord(): Boolean {
        return withContext(Dispatchers.Default) {
            val wordCount = split(' ').count { it.isNotEmpty() }
            wordCount == 1
        }
    }

    private fun RepoWithDownloadState.asItemUi(): SearchItemUi {
        return SearchItemUi(
            id = this.id,
            title = this.name,
            description = this.description,
            downloadState = when (this.downloadState) {
                Started -> ItemDownloadState.Started
                Finished -> ItemDownloadState.Finished
                NotStarted, Failed, Cancelled -> ItemDownloadState.NotStarted
            },
        )
    }

    private fun ApiError.asUiError(): UiError {
        return when (this) {
            Io -> UiError.Connection
            TooManyRequests -> UiError.TooManyRequests
            Unknown -> UiError.Unknown
        }
    }

    private suspend fun repo(id: Long): Repo {
        return withContext(Dispatchers.Default) {
            foundRepos.find { it.id == id }!!
        }
    }

    fun download(id: Long) {
        viewModelScope.launch {
            download(repo(id))
        }
    }

    fun view(id: Long) {
        viewModelScope.launch {
            view(repo(id))
        }
    }
}