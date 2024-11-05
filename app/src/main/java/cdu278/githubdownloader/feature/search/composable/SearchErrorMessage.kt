package cdu278.githubdownloader.feature.search.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cdu278.githubdownloader.R
import cdu278.githubdownloader.feature.search.SearchUi
import cdu278.githubdownloader.feature.search.SearchUi.State.Failed.Error.Connection
import cdu278.githubdownloader.feature.search.SearchUi.State.Failed.Error.TooManyRequests
import cdu278.githubdownloader.feature.search.SearchUi.State.Failed.Error.Unknown

@Composable
fun SearchErrorMessage(
    error: SearchUi.State.Failed.Error,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(
            id = when (error) {
                Connection -> R.string.error_connection
                TooManyRequests -> R.string.error_tooManyRequests
                Unknown -> R.string.error_unknown
            }
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}