package com.alilopez.kt_demohilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alilopez.kt_demohilt.core.navigation.AuthRoute
import com.alilopez.kt_demohilt.core.navigation.HireBeatFeedRoute
import com.alilopez.kt_demohilt.core.ui.theme.AppTheme
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.AuthScreen
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.FeedScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val isLoading by mainViewModel.isLoading.collectAsStateWithLifecycle()
                val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()

                if (isLoading || startDestination == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    HireBeatNavigation(startDestination = startDestination!!)
                }
            }
        }
    }
}
@Composable
fun HireBeatNavigation(startDestination: Any) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable<com.alilopez.kt_demohilt.core.navigation.AuthRoute> {
            AuthScreen(
                onNavigateToFeed = {
                    navController.navigate(com.alilopez.kt_demohilt.core.navigation.HireBeatFeedRoute) {
                        popUpTo(com.alilopez.kt_demohilt.core.navigation.AuthRoute) { inclusive = true }
                    }
                },
                onNavigateToProfileSetup = {
                    navController.navigate(com.alilopez.kt_demohilt.core.navigation.ProfileSetupRoute) {
                        popUpTo(com.alilopez.kt_demohilt.core.navigation.AuthRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<com.alilopez.kt_demohilt.core.navigation.ProfileSetupRoute> {
            com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.ProfileSetupScreen(
                onFinish = {
                    navController.navigate(HireBeatFeedRoute) {
                        popUpTo(com.alilopez.kt_demohilt.core.navigation.ProfileSetupRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<HireBeatFeedRoute> {
            FeedScreen(
                onNavigateToAuth = {
                    navController.navigate(AuthRoute) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}