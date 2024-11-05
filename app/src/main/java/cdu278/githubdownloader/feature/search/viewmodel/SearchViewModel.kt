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

    private var updatingSearchResults: Job? = null

    fun search() {
        updatingSearchResults?.cancel()
        updatingSearchResults = viewModelScope.launch {
            uiStateFlow.value = UiState.Loading
            when (val result = search(usernameFlow.value.trim())) {
                is Ok -> {
                    val reposFlow = result.value
                    reposFlow.collect { repos ->
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
        ) { user, state ->
            SearchUi(
                canSearch = user.trim().isNotEmpty(),
                state,
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