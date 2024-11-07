package cdu278.githubdownloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import cdu278.githubdownloader.core.repo.download.observer.DownloadStatesObserver
import cdu278.githubdownloader.feature.main.composable.MainScreen
import cdu278.githubdownloader.ui.theme.GitHubDownloaderTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var downloadStatesObserverFactory: DownloadStatesObserver.Factory

    private lateinit var downloadStatesObserver: DownloadStatesObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadStatesObserver = downloadStatesObserverFactory.create(lifecycleScope)

        enableEdgeToEdge()

        setContent {
            GitHubDownloaderTheme {
                MainScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        downloadStatesObserver.register(context = this)
    }

    override fun onStop() {
        super.onStop()
        downloadStatesObserver.unregister(context = this)
    }
}