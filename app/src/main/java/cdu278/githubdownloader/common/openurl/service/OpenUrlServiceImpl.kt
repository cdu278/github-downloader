package cdu278.githubdownloader.common.openurl.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OpenUrlServiceImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : OpenUrlService {

    override fun view(url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}