package com.example.metronome.ui.home

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.StopCircle
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.metronome.MetronomeApplication
import com.example.metronome.R
import com.example.metronome.ui.AppViewModelProvider
import com.example.metronome.ui.SettingsScreen
import com.example.metronome.ui.components.MetronomeConfigControls
import com.example.metronome.ui.tracks.TrackCreatorScreen
import com.example.metronome.ui.tracks.TrackEditorScreen
import com.example.metronome.ui.tracks.TrackPickerScreen
import com.example.metronome.utils.MetronomeScreen
import com.example.metronome.service.MetronomeService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeHomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = checkNotNull(MetronomeScreen.forString(
        backStackEntry?.destination?.route ?: MetronomeScreen.Home.name
    ))

    Scaffold(
        topBar = {
            MetronomeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onTrackListButtonClicked = {
                    navController.navigate(MetronomeScreen.TrackPicker.name)
                },
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
                    HomeScreen(viewModel)
                }
                composable(route = MetronomeScreen.Settings.name) {
                    SettingsScreen()
                }
                composable(route = MetronomeScreen.TrackPicker.name) {
                    TrackPickerScreen(
                        onCreateTrackButtonClicked = {
                            navController.navigate(MetronomeScreen.TrackCreator.name)
                        },
                        onSearchTrackButtonClicked = {
                            navController.navigate(MetronomeScreen.TrackSearcher.name)
                        },
                        onTrackEditClicked = {
                            navController.navigate(
                                "${MetronomeScreen.TrackEditor.name}/${it.id}"
                            )
                        },
                        onTrackPicked = {
                            viewModel.updateMetronomeConfig(it)
                            navController.navigateUp()
                        }
                    )
                }
                composable(route = MetronomeScreen.TrackCreator.name) {
                    TrackCreatorScreen(onTrackCreated = {
                        navController.navigateUp()
                    })
                }
                composable(
                    route = "${MetronomeScreen.TrackEditor.name}/{${MetronomeScreen.TrackEditor.navArgumentName}}",
                    arguments = listOf(
                        navArgument(MetronomeScreen.TrackEditor.navArgumentName!!) {
                            type = NavType.IntType
                        }
                    )
                ) {
                    TrackEditorScreen(onTrackUpdated = {
                        navController.navigateUp()
                    })
                }
                composable(route = MetronomeScreen.TrackSearcher.name) {

                }
            }
        }
    )
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel
) {
    val metronomeConfig by viewModel.metronomeConfig.collectAsState()
    val metronomeIsPlaying by viewModel.metronomeIsPlaying.collectAsState()
    val applicationContext = viewModel.getApplication<MetronomeApplication>().applicationContext

    if (!metronomeIsPlaying) {
        IconButton(
            onClick = {
                Intent(applicationContext, MetronomeService::class.java).also {
                    it.action = MetronomeService.Action.START.name
                    it.putExtra(MetronomeService.Extra.BPM.name, metronomeConfig.bpm)
                    viewModel.startMetronomeService(it)
                }
            }
        ) {
            Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null)
        }
    } else {
        IconButton(onClick = {
            Intent(applicationContext, MetronomeService::class.java).also {
                it.action = MetronomeService.Action.STOP.name
                viewModel.stopMetronomeService(it)
            }
        }) {
            Icon(imageVector = Icons.Rounded.StopCircle, contentDescription = null)
        }
    }

    MetronomeConfigControls(
        bpm = metronomeConfig.bpm,
        timeSignature = metronomeConfig.timeSignature,
        noteValue = metronomeConfig.noteValue,
        onBpmChanged = {
            viewModel.updateMetronomeConfig(metronomeConfig.copy(bpm = it))
        },
        onTimeSignaturePicked = {
            viewModel.updateMetronomeConfig(metronomeConfig.copy(timeSignature = it))
        },
        onNoteValuePicked = {
            viewModel.updateMetronomeConfig(metronomeConfig.copy(noteValue = it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetronomeAppBar(
    currentScreen: MetronomeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onTrackListButtonClicked: () -> Unit,
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
                IconButton(onClick = onTrackListButtonClicked) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = stringResource(id = R.string.track_list_button)
                    )
                }
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