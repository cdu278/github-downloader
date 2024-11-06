package cdu278.githubdownloader.feature.search.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cdu278.githubdownloader.R
import cdu278.githubdownloader.feature.search.SearchItemUi
import cdu278.githubdownloader.feature.search.SearchUi.State.Failed
import cdu278.githubdownloader.feature.search.SearchUi.State.Initial
import cdu278.githubdownloader.feature.search.SearchUi.State.Loaded
import cdu278.githubdownloader.feature.search.SearchUi.State.Loading
import cdu278.githubdownloader.feature.search.viewmodel.SearchViewModel
import cdu278.githubdownloader.ui.Margins

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        val username by viewModel.usernameFlow.collectAsStateWithLifecycle()
        val uiState by viewModel.uiFlow.collectAsStateWithLifecycle()
        SearchField(
            value = username,
            onValueChange = viewModel::inputUsername,
            canSearch = uiState.canSearch,
            search = viewModel::search,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Margins.half)
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val state = uiState.state) {
                is Initial -> { }
                is Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 70.dp)
                    )
                is Failed ->
                    SearchErrorMessage(
                        state.error,
                        modifier = Modifier
                            .padding(top = 70.dp)
                    )
                is Loaded ->
                    if (state.items.isEmpty()) {
                        Text(
                            stringResource(R.string.search_nothingFound),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 70.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(state.items, key = SearchItemUi::id) {
                                SearchResultItem(
                                    item = it,
                                    download = viewModel::download,
                                    view = viewModel::view,
                                )
                            }
                        }
                    }
            }
        }
    }
}