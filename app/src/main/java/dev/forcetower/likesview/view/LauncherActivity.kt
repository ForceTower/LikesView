package dev.forcetower.likesview.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.NavInflater
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.R
import dev.forcetower.likesview.view.launcher.LaunchDestination
import dev.forcetower.likesview.view.launcher.LauncherViewModel
import dev.forcetower.toolkit.components.BaseActivity
import dev.forcetower.toolkit.lifecycle.EventObserver
import dev.forcetower.toolkit.navigation.navigator.PermissiveNavigatorProvider
import java.util.ArrayDeque

@AndroidEntryPoint
class LauncherActivity : BaseActivity() {
    private val viewModel by viewModels<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onCreate(savedInstanceState)

        viewModel.launchDestination.observe(this, EventObserver {
            val intent = Intent(this, MainActivity::class.java)
            when (it) {
                // fake a deep link to home
                LaunchDestination.HOME -> {
                    val graph = NavInflater(this, PermissiveNavigatorProvider())
                        .inflate(R.navigation.home_graph)

                    val node = graph.findNode(R.id.home)
                    intent.putExtra("android-support-nav:controller:deepLinkIds", node!!.buildExplicitly())
                }
                LaunchDestination.LOGIN -> Unit
            }
            startActivity(intent)
            finish()
        })
    }
}

fun NavDestination.buildExplicitly(): IntArray {
    val hierarchy = ArrayDeque<NavDestination>()
    var current: NavDestination? = this
    do {
        val parent = current!!.parent
        if (parent == null || parent.startDestination != current.id) {
            hierarchy.addFirst(current)
        }
        current = parent
    } while (current != null)
    val deepLinkIds = IntArray(hierarchy.size)
    var index = 0
    for (destination in hierarchy) {
        deepLinkIds[index++] = destination.id
    }
    return deepLinkIds
}