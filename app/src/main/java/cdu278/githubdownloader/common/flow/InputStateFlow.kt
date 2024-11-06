package cdu278.githubdownloader.common.flow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> ViewModel.inputStateFlow(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialValue: () -> T,
): MutableStateFlow<T> {
    return MutableStateFlow(savedStateHandle[key] ?: initialValue())
        .also { flow ->
            flow
                .onEach { savedStateHandle[key] = it }
                .launchIn(viewModelScope)
        }
}