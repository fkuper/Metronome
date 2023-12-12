package com.fkuper.metronome

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fkuper.metronome.ui.AppViewModelProvider
import com.fkuper.metronome.ui.home.MetronomeHomeScreen
import com.fkuper.metronome.ui.theme.MetronomeTheme
import com.fkuper.metronome.utils.receivers.BootCompleteReceiver
import com.fkuper.metronome.utils.model.DisplayTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableBootCompleteReceiver()

        setContent {
            val viewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val displayTheme = viewModel.displayTheme.collectAsState()

            MetronomeTheme(darkTheme = when (displayTheme.value) {
                DisplayTheme.DARK -> true
                DisplayTheme.LIGHT -> false
                else -> isSystemInDarkTheme()
            }) {
                MetronomeHomeScreen(onThemeChanged = {
                    viewModel.setDisplayTheme(it)
                })
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // We're good! We can post notifications now
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    // Show educational UI to explain why we need the permission
                }
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                }
            }
        }
    }

    private fun enableBootCompleteReceiver() {
        ComponentName(applicationContext, BootCompleteReceiver::class.java).also {
            applicationContext.packageManager.setComponentEnabledSetting(
                it,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

}