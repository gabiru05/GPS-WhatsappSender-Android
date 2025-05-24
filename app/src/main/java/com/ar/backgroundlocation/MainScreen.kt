package com.ar.backgroundlocation // Reemplaza con tu paquete

import android.content.Context // Necesario para el Toast y el Intent de servicio
import android.content.Intent
import android.location.Location // Para el tipo de currentLocation
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview // Si quieres mantener el Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLEncoder // Para la URL de WhatsApp

// Función de extensión para formatear Double (si no la tienes global)
fun Double.format(digits: Int): String = "%.${digits}f".format(this)


@OptIn(ExperimentalMaterial3Api::class) // Para OutlinedTextField y otros componentes M3
@Composable
fun App(currentLocation: Location?) { // Recibe la ubicación actual
    val context = LocalContext.current
    // Estados para los nuevos campos de texto
    var phoneNumber by remember { mutableStateOf("+507") }
    var customMessage by remember { mutableStateOf("") }
    // Estado para saber si el servicio está corriendo (puedes mejorarlo observando un estado real del servicio)
    var isServiceRunningActually by remember { mutableStateOf(false) } // Estimación inicial

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Sección "Push Notification Service" y Lat/Lon ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_send), // Icono placeholder
                contentDescription = "Service Status Icon",
                modifier = Modifier.size(20.dp) // Un poco más pequeño
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Push Notification Service", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.weight(1f)) // Empuja Lat/Lon a la derecha
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "Lat: ${currentLocation?.latitude?.format(6) ?: "---"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "Lon: ${currentLocation?.longitude?.format(6) ?: "---"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // --- Botones para iniciar/detener servicio (como los tenías) ---
        // Puedes decidir si quieres mantenerlos visibles o controlar el servicio de otra forma
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                Toast.makeText(context, "Iniciando servicio...", Toast.LENGTH_SHORT).show()
                Intent(context, LocationService::class.java).apply {
                    action = LocationService.ACTION_SERVICE_START
                    context.startService(this)
                }
                isServiceRunningActually = true // Actualiza estado local
            }) {
                Text(text = "Iniciar GPS")
            }
            Button(onClick = {
                Toast.makeText(context, "Deteniendo servicio...", Toast.LENGTH_SHORT).show()
                Intent(context, LocationService::class.java).apply {
                    action = LocationService.ACTION_SERVICE_STOP
                    context.startService(this)
                }
                isServiceRunningActually = false // Actualiza estado local
            }) {
                Text(text = "Detener GPS")
            }
        }
        Text(
            text = if (isServiceRunningActually) "Servicio GPS: Corriendo" else "Servicio GPS: Detenido",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))


        // --- Nuevos Elementos para WhatsApp (basados en tu imagen) ---
        Text(
            text = "Estoy perdido, por favor encuentrenme",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Image(
            // Asegúrate de que ic_person_placeholder exista en res/drawable
            painter = painterResource(id = R.drawable.ic_person_placeholder),
            contentDescription = "Icono de Usuario",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número WhatsApp (+CódPaís)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = customMessage,
            onValueChange = { customMessage = it },
            label = { Text("Mensaje (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp), // Para que sea un área de texto más grande
        )

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

        Button(
            onClick = {
                if (currentLocation?.latitude != null && currentLocation.longitude != null) {
                    val completeMessage = buildString {
                        if (customMessage.isNotBlank()) {
                            append(customMessage)
                            append("\n\n")
                        }
                        append("¡Ayuda! Esta es mi ubicación:\n")
                        append("Latitud: ${currentLocation.latitude.format(6)}\n")
                        append("Longitud: ${currentLocation.longitude.format(6)}\n\n")
                        // Enlace de Google Maps funcional
                        append("https://www.google.com/maps?q=${currentLocation.latitude.format(6)},${currentLocation.longitude.format(6)}")
                    }
                    // Llama a la función sendWhatsAppMessage que ahora está en MainActivity
                    (context as? MainActivity)?.sendWhatsAppMessage(context, phoneNumber, completeMessage)
                } else {
                    Toast.makeText(context, "Ubicación no disponible. Asegúrate de que el servicio GPS esté activo y con permisos.", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp) // Ajustar padding
        ) {
            Text("Enviar a Whatsapp", fontSize = 16.sp)
        }
    }
}

// Si quieres un Preview para el App Composable
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // Asume que tienes un tema definido en ui.theme, si no, envuélvelo con MaterialTheme()
    // com.ar.backgroundlocation.ui.theme.BackGroundLocationTheme {
    App(currentLocation = Location("preview").apply { latitude = 10.0; longitude = -75.0 })
    // }
}