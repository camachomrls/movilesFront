package com.alilopez.kt_demohilt.features.HireBeat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile
import com.alilopez.kt_demohilt.features.HireBeat.presentation.components.BackgroundColor
import com.alilopez.kt_demohilt.features.HireBeat.presentation.components.PrimaryColor
import com.alilopez.kt_demohilt.features.HireBeat.presentation.components.TextDark
import com.alilopez.kt_demohilt.features.HireBeat.presentation.viewmodels.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit // Callback para cuando el usuario cierre sesión
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HireBeat", fontWeight = FontWeight.Black, color = PrimaryColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor),
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogoutSuccess = onNavigateToAuth) }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = PrimaryColor)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Feed") },
                    label = { Text("Feed") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryColor, indicatorColor = PrimaryColor.copy(alpha = 0.2f))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryColor, indicatorColor = PrimaryColor.copy(alpha = 0.2f))
                )
            }
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryColor)
                }
                uiState.error != null -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = uiState.error!!, color = Color.Red, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.logout(onLogoutSuccess = onNavigateToAuth) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                        ) {
                            Text("Volver al Login")
                        }
                    }
                }
                uiState.profile != null -> {
                    FeedContent(profile = uiState.profile!!)
                }
            }
        }
    }
}

@Composable
fun FeedContent(profile: UserProfile) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Saludo Personalizado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(PrimaryColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = profile.fullName.take(1).uppercase(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Hola, ${profile.fullName.split(" ").first()}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text(text = if (profile.role == "Musician") "Músico" else "Reclutador", color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Descubre", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextDark)
        }

        // Aquí simulamos tarjetas de contenido dependiendo del rol
        items(5) { index ->
            Card(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (profile.role == "Musician") "Oferta de banda #${index + 1}" else "Músico disponible #${index + 1}",
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }
    }
}