package com.alilopez.kt_demohilt.features.HireBeat.presentation.screens

import android.Manifest // IMPORTANTE: Agregado para permisos
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.rememberLauncherForActivityResult // IMPORTANTE
import androidx.activity.result.contract.ActivityResultContracts // IMPORTANTE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alilopez.kt_demohilt.features.HireBeat.presentation.viewmodels.ProfileSetupViewModel
import kotlin.math.sqrt

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var showLinkDialog by remember { mutableStateOf(false) }
    var linkName by remember { mutableStateOf("") }
    var linkRef by remember { mutableStateOf("") }
    var selectedLinkType by remember { mutableStateOf(1) }

    var showInstrumentDialog by remember { mutableStateOf(false) }
    var tempSelectedInstrumentId by remember { mutableStateOf<Int?>(null) }
    var selectedLevel by remember { mutableStateOf(1) } // 1: Básico, 2: Medio, 3: Pro
    var isPrincipal by remember { mutableStateOf(true) }

    // --- CONFIGURACIÓN DEL ACELERÓMETRO ---
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    val gX = event.values[0] / SensorManager.GRAVITY_EARTH
                    val gY = event.values[1] / SensorManager.GRAVITY_EARTH
                    val gZ = event.values[2] / SensorManager.GRAVITY_EARTH

                    val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()
                    if (gForce > 2.5f) {
                        viewModel.clearForm()
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose { sensorManager.unregisterListener(listener) }
    }

    // --- LANZADOR PARA PEDIR PERMISOS DE UBICACIÓN EN TIEMPO DE EJECUCIÓN ---
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.detectCity() // Si el usuario da permiso, arranca la detección
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onFinish()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Completa tu perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            state.error?.let { errorMsg ->
                Text(text = "Error: $errorMsg", color = MaterialTheme.colorScheme.error)
            }

            SetupCard(title = "Sobre ti") {
                OutlinedTextField(
                    value = state.descripcion,
                    onValueChange = { viewModel.onDescripcionChange(it) },
                    label = { Text("Biografía / Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    OutlinedTextField(
                        value = state.city,
                        onValueChange = { viewModel.onCityChange(it) },
                        label = { Text("Ciudad") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = {
                                // LANZA LA PETICIÓN DEL PERMISO
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Detectar Ciudad")
                            }
                        }
                    )

                    OutlinedTextField(
                        value = state.experience,
                        onValueChange = { viewModel.onExperienceChange(it) },
                        label = { Text("Años Exp.") },
                        modifier = Modifier.width(120.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            SetupCard(title = "Tus Instrumentos") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.availableInstruments.forEach { instrument ->
                        val isSelected = state.selectedInstruments.any { it.instrumentId == instrument.id }

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    viewModel.removeInstrument(instrument.id)
                                } else {
                                    tempSelectedInstrumentId = instrument.id
                                    selectedLevel = 1
                                    isPrincipal = false
                                    showInstrumentDialog = true
                                }
                            },
                            label = { Text(instrument.name) }
                        )
                    }
                }
            }

            SetupCard(title = "Géneros Musicales") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.availableGenres.forEach { genre ->
                        val isSelected = state.selectedGenres.contains(genre.id)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.toggleGenre(genre.id) },
                            label = { Text(genre.name) }
                        )
                    }
                }
            }

            SetupCard(title = "Enlaces a Plataformas") {
                if (state.links.isEmpty()) {
                    Text("Aún no has agregado enlaces", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    state.links.forEachIndexed { index, link ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            val typeLabel = if(link.type == 1) "Video" else "Audio"
                            Text("${link.name} ($typeLabel): ${link.ref}", modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.removeLink(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showLinkDialog = true },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Enlace")
                }
            }

            val isFormValid = state.descripcion.isNotBlank() &&
                    state.city.isNotBlank() &&
                    state.experience.isNotBlank() &&
                    state.selectedInstruments.isNotEmpty() &&
                    state.selectedGenres.isNotEmpty()

            Button(
                onClick = { viewModel.onSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading && isFormValid
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Perfil", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // --- DIALOG PARA INSTRUMENTOS ---
        if (showInstrumentDialog && tempSelectedInstrumentId != null) {
            AlertDialog(
                onDismissRequest = { showInstrumentDialog = false },
                title = { Text("Detalle del Instrumento") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("¿Cuál es tu nivel?", fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                            FilterChip(selected = selectedLevel == 1, onClick = { selectedLevel = 1 }, label = { Text("Básico") })
                            FilterChip(selected = selectedLevel == 2, onClick = { selectedLevel = 2 }, label = { Text("Medio") })
                            FilterChip(selected = selectedLevel == 3, onClick = { selectedLevel = 3 }, label = { Text("Avanzado") })
                        }
                        Text("Prioridad", fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isPrincipal, onCheckedChange = { isPrincipal = it })
                            Text("Es mi instrumento principal")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addInstrument(tempSelectedInstrumentId!!, selectedLevel, isPrincipal)
                        showInstrumentDialog = false
                    }) { Text("Guardar", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showInstrumentDialog = false }) { Text("Cancelar") }
                }
            )
        }

        // --- DIALOG PARA ENLACES ---
        if (showLinkDialog) {
            AlertDialog(
                onDismissRequest = { showLinkDialog = false },
                title = { Text("Nueva Plataforma") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Tipo de Enlace", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected = selectedLinkType == 1, onClick = { selectedLinkType = 1 }, label = { Text("Video (YouTube, etc)") })
                            FilterChip(selected = selectedLinkType == 2, onClick = { selectedLinkType = 2 }, label = { Text("Audio (Spotify, etc)") })
                        }

                        OutlinedTextField(
                            value = linkName,
                            onValueChange = { linkName = it },
                            label = { Text("Nombre (Ej: Spotify)") }
                        )
                        OutlinedTextField(
                            value = linkRef,
                            onValueChange = { linkRef = it },
                            label = { Text("URL (@usuario o link)") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addLink(name = linkName, type = selectedLinkType, ref = linkRef)
                        linkName = ""
                        linkRef = ""
                        selectedLinkType = 1
                        showLinkDialog = false
                    }) { Text("Guardar", fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(onClick = { showLinkDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun SetupCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}