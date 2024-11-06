package cdu278.githubdownloader.feature.main

import kotlinx.serialization.Serializable

interface MainRoutes {

    @Serializable
    object Search

    @Serializable
    object Downloads
}