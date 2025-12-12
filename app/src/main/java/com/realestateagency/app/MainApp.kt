package com.realestateagency.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.realestateagency.app.models.Agent
import com.realestateagency.app.models.Property
import com.realestateagency.app.models.ShowingRequest
import com.realestateagency.app.presentation.viewmodel.AppViewModel
import com.realestateagency.app.ui.screens.DetailScreen
import com.realestateagency.app.ui.screens.FavoritesScreen
import com.realestateagency.app.ui.screens.HomeScreen
import com.realestateagency.app.ui.screens.ShowingRequestScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainApp() {
    val viewModel: AppViewModel = viewModel()
    val navController = rememberNavController()
    val properties by viewModel.properties.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedAgent by remember { mutableStateOf<Agent?>(null) }
    var favorites by remember { mutableStateOf(setOf<Int>()) }
    val scope = rememberCoroutineScope()

    MainContent(
        navController = navController,
        properties = properties,
        isLoading = isLoading,
        favorites = favorites,
        selectedAgent = selectedAgent,
        scope = scope,
        onFavoritesChange = { favorites = it },
        onSelectedAgentChange = { selectedAgent = it }
    )
}

@Composable
private fun MainContent(
    navController: NavHostController,
    properties: List<Property>,
    isLoading: Boolean,
    favorites: Set<Int>,
    selectedAgent: Agent?,
    scope: CoroutineScope,
    onFavoritesChange: (Set<Int>) -> Unit,
    onSelectedAgentChange: (Agent?) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                if (currentRoute in listOf("home", "favorites")) {
                    BottomNavigationBar(currentRoute, navController)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppNavigation(
                    navController = navController,
                    properties = properties,
                    isLoading = isLoading,
                    favorites = favorites,
                    selectedAgent = selectedAgent,
                    scope = scope,
                    onFavoritesChange = onFavoritesChange,
                    onSelectedAgentChange = onSelectedAgentChange
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    currentRoute: String?,
    navController: NavHostController
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
            label = { Text("Главная") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Избранное") },
            label = { Text("Избранное") },
            selected = currentRoute == "favorites",
            onClick = {
                navController.navigate("favorites") {
                    popUpTo("favorites") { inclusive = true }
                }
            }
        )
    }
}

@Composable
private fun AppNavigation(
    navController: NavHostController,
    properties: List<Property>,
    isLoading: Boolean,
    favorites: Set<Int>,
    selectedAgent: Agent?,
    scope: CoroutineScope,
    onFavoritesChange: (Set<Int>) -> Unit,
    onSelectedAgentChange: (Agent?) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") {
            HomeScreen(
                properties = properties,
                favorites = favorites,
                isLoading = isLoading,
                onPropertyClick = { property ->
                    scope.launch {
                        onSelectedAgentChange(null)
                        navController.navigate("detail/${property.id}")
                    }
                },
                onFavoriteClick = { property ->
                    val newFavorites = if (property.id in favorites) {
                        favorites - property.id
                    } else {
                        favorites + property.id
                    }
                    onFavoritesChange(newFavorites)
                },
                onSearchClick = {}
            )
        }

        composable("favorites") {
            FavoritesScreen(
                properties = properties,
                favorites = favorites,
                onPropertyClick = { property ->
                    scope.launch {
                        onSelectedAgentChange(null)
                        navController.navigate("detail/${property.id}")
                    }
                },
                onRemoveFavorite = { property ->
                    onFavoritesChange(favorites - property.id)
                }
            )
        }

        composable(
            route = "detail/{propertyId}",
            arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getInt("propertyId") ?: 0
            val property = properties.find { it.id == propertyId }
            if (property != null) {
                DetailScreen(
                    property = property,
                    agent = selectedAgent,
                    onBack = { navController.popBackStack() },
                    onRequestShowing = {
                        navController.navigate("showing/${propertyId}")
                    }
                )
            }
        }

        composable(
            route = "showing/{propertyId}",
            arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
        ) { backStackEntry ->
            ShowingRequestComposable(
                backStackEntry = backStackEntry,
                properties = properties,
                navController = navController,
                scope = scope
            )
        }
    }
}

@Composable
private fun ShowingRequestComposable(
    backStackEntry: androidx.navigation.NavBackStackEntry,
    properties: List<Property>,
    navController: NavHostController,
    scope: CoroutineScope
) {
    val propertyId = backStackEntry.arguments?.getInt("propertyId") ?: 0
    val property = properties.find { it.id == propertyId }
    var isSubmitting by remember { mutableStateOf(false) }

    if (property != null) {
        ShowingRequestScreen(
            property = property,
            onBack = { navController.popBackStack() },
            onSubmit = { name, phone, email, date, message ->
                isSubmitting = true
                scope.launch {
                    val request = ShowingRequest(
                        propertyId = propertyId,
                        agentId = property.agentId,
                        clientName = name,
                        clientPhone = phone,
                        clientEmail = email,
                        preferredDate = date,
                        message = message
                    )
                    try {
                        // Будущая реализация
                        isSubmitting = false
                        navController.popBackStack()
                        navController.popBackStack()
                    } catch (e: Exception) {
                        isSubmitting = false
                    }
                }
            },
            isSubmitting = isSubmitting
        )
    }
}
