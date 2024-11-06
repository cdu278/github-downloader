package cdu278.githubdownloader.feature.main.composable

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cdu278.githubdownloader.feature.downloads.composable.DownloadsScreen
import cdu278.githubdownloader.feature.main.MainRoutes
import cdu278.githubdownloader.feature.search.composable.SearchScreen

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            MainNavigationBar(navController)
        },
        modifier = modifier
    ) { paddings ->
        NavHost(
            navController,
            startDestination = MainRoutes.Search,
            modifier = Modifier
                .padding(paddings)
                .consumeWindowInsets(paddings)
        ) {
            composable<MainRoutes.Search> {
                SearchScreen(
                    viewModel = hiltViewModel(),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable<MainRoutes.Downloads> {
                DownloadsScreen(
                    viewModel = hiltViewModel(),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
