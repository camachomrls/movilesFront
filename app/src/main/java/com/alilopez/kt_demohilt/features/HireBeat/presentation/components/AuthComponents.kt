package com.alilopez.kt_demohilt.features.HireBeat.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alilopez.kt_demohilt.features.HireBeat.domain.models.UserRole

val PrimaryColor = Color(0xFF9B3922)
val BackgroundColor = Color(0xFFF8EFE5)
val SurfaceColor = Color.White
val TextDark = Color(0xFF4A2C21)
val AccentColor = Color(0xFF3F6B67)

@Composable
fun AuthTabs(isLogin: Boolean, onTabSelected: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceColor.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
            .padding(4.dp)
    ) {
        TabButton(
            text = "Iniciar Sesión",
            isSelected = isLogin,
            onClick = { onTabSelected(true) },
            modifier = Modifier.weight(1f)
        )
        TabButton(
            text = "Registrarse",
            isSelected = !isLogin,
            onClick = { onTabSelected(false) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) PrimaryColor else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color(0xFF8B5E4A)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = if (isSelected) ButtonDefaults.buttonElevation(4.dp) else null,
        modifier = modifier.height(48.dp)
    ) {
        Text(text = text.uppercase(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RoleSelector(currentRole: UserRole, onRoleSelected: (UserRole) -> Unit) {
    Column {
        Text(text = "¿Qué estás buscando?", fontWeight = FontWeight.Black, color = TextDark, modifier = Modifier.padding(bottom = 16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RoleCard(
                title = "Soy Músico",
                isSelected = currentRole == UserRole.MUSICIAN,
                onClick = { onRoleSelected(UserRole.MUSICIAN) },
                modifier = Modifier.weight(1f)
            )
            RoleCard(
                title = "Busco Músicos",
                isSelected = currentRole == UserRole.RECRUITER,
                onClick = { onRoleSelected(UserRole.RECRUITER) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RoleCard(title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.dp, if (isSelected) AccentColor else Color.White),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AccentColor.copy(alpha = 0.05f) else SurfaceColor
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = if (isSelected) AccentColor else Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.Bold, color = if (isSelected) AccentColor else Color.Gray)
        }
    }
}

@Composable
fun HireBeatTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontWeight = FontWeight.Bold) },
        shape = RoundedCornerShape(24.dp),
        // CORRECCIÓN PARA MATERIAL 3 APLICADA AQUÍ:
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceColor,
            unfocusedContainerColor = SurfaceColor,
            focusedBorderColor = Color(0xFFD99C38),
            unfocusedBorderColor = Color.Transparent,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}