package cdu278.githubdownloader.feature.search.composable

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import cdu278.githubdownloader.R

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    canSearch: Boolean,
    search: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value,
        onValueChange,
        label = { Text(stringResource(R.string.search)) },
        placeholder = { Text(stringResource(R.string.username)) },
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = search,
                enabled = canSearch,
            ) {
                Icon(painterResource(R.drawable.ic_search), contentDescription = null)
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