package com.example.metronome.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.metronome.R
import com.example.metronome.ui.components.BpmPicker
import com.example.metronome.ui.components.NotePicker
import com.example.metronome.ui.components.TimeSignaturePicker
import com.example.metronome.utils.MetronomeConfig
import com.example.metronome.utils.MetronomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeHomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val metronomeConfig by viewModel.metronomeConfig.collectAsState()
    val currentScreen = MetronomeScreen.valueOf(
        backStackEntry?.destination?.route ?: MetronomeScreen.Home.name
    )

    Scaffold(
        topBar = {
            MetronomeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onSettingsButtonClicked = {
                    navController.navigate(MetronomeScreen.Settings.name)
                },
            )
        },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = MetronomeScreen.Home.name,
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                composable(route = MetronomeScreen.Home.name) {
                    HomeScreen(metronomeConfig = metronomeConfig)
                }
                composable(route = MetronomeScreen.Settings.name) {
                    SettingsScreen()
                }
            }
        }
    )
}

@Composable
private fun HomeScreen(
    metronomeConfig: MetronomeConfig
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BpmPicker(
            bpm = metronomeConfig.bpm,
            onBpmChanged = {
                // TODO
            },
            modifier = Modifier.padding(8.dp)
        )
        Column(modifier = Modifier.padding(8.dp)) {
            TimeSignaturePicker(
                timeSignature = metronomeConfig.timeSignature,
                onTimeSignaturePicked = {
                    // TODO
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            NotePicker(
                noteValue = metronomeConfig.noteValue,
                onNoteValuePicked = {
                    // TODO
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetronomeAppBar(
    currentScreen: MetronomeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (currentScreen == MetronomeScreen.Home) {
                IconButton(onClick = onSettingsButtonClicked) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.settings_button)
                    )
                }
            }
        }
    )
}