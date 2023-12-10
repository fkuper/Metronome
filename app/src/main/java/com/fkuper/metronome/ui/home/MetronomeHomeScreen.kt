package com.fkuper.metronome.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fkuper.metronome.R
import com.fkuper.metronome.ui.AppViewModelProvider
import com.fkuper.metronome.ui.SettingsScreen
import com.fkuper.metronome.ui.tracks.TrackCreatorScreen
import com.fkuper.metronome.ui.tracks.TrackEditorScreen
import com.fkuper.metronome.ui.tracks.TrackPickerScreen
import com.fkuper.metronome.ui.tracks.TrackSearcherScreen
import com.fkuper.metronome.utils.DisplayTheme
import com.fkuper.metronome.utils.MetronomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeHomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onThemeChanged: (DisplayTheme) -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = checkNotNull(
        MetronomeScreen.forString(
        backStackEntry?.destination?.route ?: MetronomeScreen.Home.name
    ))
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                    SettingsScreen(onThemeChanged = onThemeChanged)
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
                    TrackSearcherScreen(snackbarHostState = snackbarHostState)
                }
            }
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