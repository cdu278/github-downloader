package cdu278.githubdownloader.feature.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cdu278.githubdownloader.R

class MainTab(
    val route: Any,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
)

val MainTabs =
    listOf(
        MainTab(
            route = MainRoutes.Search,
            title = R.string.search,
            icon = R.drawable.ic_search,
        ),
        MainTab(
            route = MainRoutes.Downloads,
            title = R.string.downloads,
            icon = R.drawable.ic_download,
        ),
    )