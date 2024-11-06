package cdu278.githubdownloader.feature.search.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import cdu278.githubdownloader.R

@Composable
fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    canSearch: Boolean,
    search: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val focusRequester = remember { FocusRequester() }
        SearchField(
            value,
            onValueChange,
            canSearch,
            search = search,
            clear = {
                onValueChange("")
                focusRequester.requestFocus()
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
        )
        SearchButton(
            onClick = search,
            enabled = canSearch,
        )
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    canSearch: Boolean,
    search: () -> Unit,
    clear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value,
        onValueChange,
        label = { Text(stringResource(R.string.search)) },
        placeholder = { Text(stringResource(R.string.username)) },
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                ClearButton(onClick = clear)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (canSearch) ImeAction.Search else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onSearch = { search() },
        ),
        modifier = modifier
    )
}

@Composable
private fun ClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(painterResource(R.drawable.ic_clear), contentDescription = null)
    }
}

@Composable
private fun SearchButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(painterResource(R.drawable.ic_search), contentDescription = null)
    }
}