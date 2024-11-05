package cdu278.githubdownloader.util

import kotlinx.coroutines.flow.SharingStarted

val UiSharingStarted = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)