package cdu278.githubdownloader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cdu278.githubdownloader.core.repo.download.observer.DownloadStatesObserver
import cdu278.githubdownloader.feature.main.composable.MainScreen
import cdu278.githubdownloader.ui.theme.GitHubDownloaderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var downloadStatesObserver: DownloadStatesObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GitHubDownloaderTheme {
                MainScreen()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                downloadStatesObserver.observe(context = this@MainActivity)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        downloadStatesObserver.unregister(context = this)
    }
}