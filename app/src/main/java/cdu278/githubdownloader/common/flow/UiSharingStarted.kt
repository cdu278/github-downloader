package cdu278.githubdownloader.common.flow

import kotlinx.coroutines.flow.SharingStarted

val UiSharingStarted = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)