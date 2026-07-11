package com.techegrity.stream_view.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techegrity.stream_view.feature.list.StreamListRoute
import com.techegrity.stream_view.feature.player.StreamPlayerRoute
import com.techegrity.stream_view.feature.player.StreamPlayerViewModel

@Composable
fun StreamViewNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = StreamViewRoutes.LIST,
        modifier = modifier,
    ) {
        composable(
            route = StreamViewRoutes.LIST,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            StreamListRoute(
                onNavigateToPlayer = { streamId ->
                    navController.navigate(StreamViewRoutes.player(streamId))
                },
            )
        }
        composable(
            route = StreamViewRoutes.PLAYER,
            arguments = listOf(
                navArgument(StreamPlayerViewModel.ARG_STREAM_ID) {
                    type = NavType.StringType
                },
            ),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            StreamPlayerRoute(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
