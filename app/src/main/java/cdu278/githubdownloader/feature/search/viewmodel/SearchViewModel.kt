package cdu278.githubdownloader.feature.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cdu278.githubdownloader.core.ApiError
import cdu278.githubdownloader.core.ApiError.Io
import cdu278.githubdownloader.core.ApiError.TooManyRequests
import cdu278.githubdownloader.core.ApiError.Unknown
import cdu278.githubdownloader.core.Result.Failure
import cdu278.githubdownloader.core.Result.Ok
import cdu278.githubdownloader.core.repo.download.DownloadableRepo
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Failed
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Finished
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.NotStarted
import cdu278.githubdownloader.core.repo.download.RepoDownloadState.Started
import cdu278.githubdownloader.core.repo.search.usecase.SearchRepoUseCase
import cdu278.githubdownloader.feature.search.SearchItemUi
import cdu278.githubdownloader.feature.search.SearchUi
import cdu278.githubdownloader.util.UiSharingStarted
import cdu278.githubdownloader.util.inputStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
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

    private val searchUserFlow = MutableSharedFlow<String>()

    fun search() {
        viewModelScope.launch { searchUserFlow.emit(usernameFlow.value.trim()) }
    }

    private val loadingFlow = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchResultFlow =
        searchUserFlow.transformLatest { username ->
            val result = try {
                loadingFlow.value = true
                search(username)
            } finally {
                loadingFlow.value = false
            }
            when (result) {
                is Ok -> {
                    val reposFlow = result.value
                    emitAll(reposFlow.map { Ok(it) })
                }
                is Failure -> emit(result)
            }
        }.stateIn(viewModelScope, UiSharingStarted, initialValue = null)

    val uiStateFlow: StateFlow<SearchUi> =
        combine(
            usernameFlow,
            searchResultFlow,
            loadingFlow,
        ) { user, searchResult, loading ->
            SearchUi(
                canSearch = user.trim().isNotEmpty(),
                state = if (loading) {
                    UiState.Loading
                } else {
                    when (searchResult) {
                        null -> UiState.Initial
                        is Ok -> {
                            val repos = searchResult.value
                            UiState.Loaded(
                                items = withContext(Dispatchers.Default) {
                                    repos
                                        .map { it.asItemUi() }
                                        .toImmutableList()
                                }
                            )
                        }
                        is Failure -> UiState.Failed(searchResult.error.asUiError())
                    }
                }
            )
        }.stateIn(viewModelScope, UiSharingStarted, initialValue = SearchUi())

    private fun DownloadableRepo.asItemUi(): SearchItemUi {
        return SearchItemUi(
            id = this.id,
            title = this.name,
            description = this.description,
            downloadState = when (this.downloadState) {
                Started -> ItemDownloadState.Started
                Finished -> ItemDownloadState.Finished
                NotStarted, Failed -> ItemDownloadState.NotStarted
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
}