package com.alilopez.kt_demohilt.features.HireBeat.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alilopez.kt_demohilt.features.HireBeat.domain.models.UserRole
import com.alilopez.kt_demohilt.features.HireBeat.presentation.components.AuthTabs
import com.alilopez.kt_demohilt.features.HireBeat.presentation.viewmodels.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToFeed: () -> Unit,
    onNavigateToProfileSetup: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            if (uiState.isNewMusician) {
                onNavigateToProfileSetup()
            } else {
                onNavigateToFeed()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título principal
            Text(
                text = if (uiState.isLogin) "Bienvenido de nuevo" else "Crea tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "HireBeat: La red para músicos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Tabs para cambiar entre Login y Registro
            AuthTabs(
                isLogin = uiState.isLogin,
                onTabSelected = { viewModel.onToggleLogin() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- CAMPOS DEL FORMULARIO ---

            // 1. Campo Nombre Completo (SOLO EN REGISTRO)
            if (!uiState.isLogin) {
                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { viewModel.onFullNameChange(it) },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Campo Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Campo Contraseña
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            // 4. Selector de Rol (SOLO EN REGISTRO)
            if (!uiState.isLogin) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "¿Cómo quieres usar HireBeat?",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Start)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.role == UserRole.MUSICIAN,
                        onClick = { viewModel.onRoleChange(UserRole.MUSICIAN) }
                    )
                    Text("Soy Músico")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = uiState.role == UserRole.RECRUITER,
                        onClick = { viewModel.onRoleChange(UserRole.RECRUITER) }
                    )
                    Text("Busco Músicos")
                }
            }

            // --- MENSAJES DE ERROR ---
            uiState.error?.let { errorMsg ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), // <-- Esto estaba dando el error
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (uiState.isLogin) "Iniciar Sesión" else "Registrarse",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}