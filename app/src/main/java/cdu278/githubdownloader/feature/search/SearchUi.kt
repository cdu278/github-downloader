package cdu278.githubdownloader.feature.search

import kotlinx.collections.immutable.ImmutableList

data class SearchUi(
    val canSearch: Boolean = false,
    val state: State = State.Initial,
) {

    sealed interface State {

        data object Initial : State

        data object Loading : State

        data class Loaded(val items: ImmutableList<SearchItemUi>) : State

        data class Failed(val error: Error) : State {

            enum class Error {

                Connection,
                TooManyRequests,
                Unknown
            }
        }
    }
}