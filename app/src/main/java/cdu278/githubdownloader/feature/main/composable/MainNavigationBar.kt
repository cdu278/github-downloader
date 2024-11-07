package cdu278.githubdownloader.feature.main.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import cdu278.githubdownloader.feature.main.MainTabs

@Composable
fun MainNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val destination = backStackEntry?.destination
        MainTabs.forEach { tab ->
            NavigationBarItem(
                label = { Text(stringResource(tab.title)) },
                icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                selected = destination?.hierarchy
                    ?.any { it.hasRoute(tab.route::class) } == true,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}