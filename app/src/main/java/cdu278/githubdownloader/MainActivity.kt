package cdu278.githubdownloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cdu278.githubdownloader.feature.main.composable.MainScreen
import cdu278.githubdownloader.ui.theme.GitHubDownloaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GitHubDownloaderTheme {
                MainScreen()
            }
        }
    }
}