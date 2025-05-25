package com.ar.backgroundlocation

import android.content.Context
import android.content.Intent
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.draw.clip // No es necesario si quitamos .clip()
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLEncoder

fun Double.format(digits: Int): String = "%.${digits}f".format(this)

object AppScreenColors {
    val ScreenBackground = Color(0xFFF0F7FA)
    val CardBackground = Color.White
    val PrimaryAction = Color(0xFF00A0B0)
    val PrimaryActionText = Color.White
    val SecondaryActionOutline = Color(0xFF00A0B0)
    val SecondaryActionText = Color(0xFF007C89)

    val TitleText = Color(0xFF003D44)
    val PrimaryText = Color(0xFF264653)
    val SecondaryText = Color(0xFF58737F)
    val HintText = Color(0xFF8AA3AF)

    val IconPrimaryTint = Color(0xFF007C89)
    val IconPersonPlaceholder = Color(0xFFAFBCC3)

    val TextFieldBorder = Color(0xFFCED9E0)
    val TextFieldFocusedBorder = PrimaryAction
    val TextFieldCursor = PrimaryAction
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(currentLocation: Location?) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("+507") }
    var customMessage by remember { mutableStateOf("") }
    var isServiceRunningUiState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppScreenColors.ScreenBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.cardColors(containerColor = AppScreenColors.CardBackground),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ícono Servicio GPS",
                        tint = AppScreenColors.IconPrimaryTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Servicio de Localización GPS",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = AppScreenColors.PrimaryText
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Lat: ${currentLocation?.latitude?.format(6) ?: "---"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppScreenColors.SecondaryText
                    )
                    Text(
                        "Lon: ${currentLocation?.longitude?.format(6) ?: "---"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppScreenColors.SecondaryText
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Iniciando servicio GPS...", Toast.LENGTH_SHORT).show()
                            Intent(context, LocationService::class.java).apply {
                                action = LocationService.ACTION_SERVICE_START
                                context.startService(this)
                            }
                            isServiceRunningUiState = true
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppScreenColors.SecondaryActionText),

                    ) {
                        Text("Iniciar GPS")
                    }
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Deteniendo servicio GPS...", Toast.LENGTH_SHORT).show()
                            Intent(context, LocationService::class.java).apply {
                                action = LocationService.ACTION_SERVICE_STOP
                                context.startService(this)
                            }
                            isServiceRunningUiState = false
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AppScreenColors.SecondaryActionText),
                       
                    ) {
                        Text("Detener GPS")
                    }
                }
                Text(
                    text = if (isServiceRunningUiState) "Servicio GPS: Solicitado Iniciar" else "Servicio GPS: Solicitado Detener / Inactivo",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppScreenColors.SecondaryText,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Estoy perdido, por favor encuentrenme",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AppScreenColors.TitleText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_person_placeholder),
            contentDescription = "Icono de Usuario",
            modifier = Modifier
                .size(90.dp)
                .padding(bottom = 20.dp)
            // .clip(RoundedCornerShape(45.dp)), // LÍNEA ELIMINADA
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número WhatsApp (+CódPaís)") },
            placeholder = { Text("+5076XXXXXXX", color = AppScreenColors.HintText) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = AppScreenColors.PrimaryText,
                unfocusedTextColor = AppScreenColors.PrimaryText,
                cursorColor = AppScreenColors.TextFieldCursor,
                focusedBorderColor = AppScreenColors.TextFieldFocusedBorder,
                unfocusedBorderColor = AppScreenColors.TextFieldBorder,
                focusedLabelColor = AppScreenColors.PrimaryAction,
                unfocusedLabelColor = AppScreenColors.SecondaryText,
                focusedPlaceholderColor = AppScreenColors.HintText,
                unfocusedPlaceholderColor = AppScreenColors.HintText
            )
        )

        OutlinedTextField(
            value = customMessage,
            onValueChange = { customMessage = it },
            label = { Text("Mensaje adicional (opcional)") },
            placeholder = { Text("Ej: Necesito ayuda cerca de...", color = AppScreenColors.HintText)},
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 150.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = AppScreenColors.PrimaryText,
                unfocusedTextColor = AppScreenColors.PrimaryText,
                cursorColor = AppScreenColors.TextFieldCursor,
                focusedBorderColor = AppScreenColors.TextFieldFocusedBorder,
                unfocusedBorderColor = AppScreenColors.TextFieldBorder,
                focusedLabelColor = AppScreenColors.PrimaryAction,
                unfocusedLabelColor = AppScreenColors.SecondaryText,
                focusedPlaceholderColor = AppScreenColors.HintText,
                unfocusedPlaceholderColor = AppScreenColors.HintText
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                currentLocation?.let { loc ->
                    val lat = loc.latitude
                    val lon = loc.longitude
                    val completeMessage = buildString {
                        if (customMessage.isNotBlank()) {
                            append(customMessage.trim())
                            append("\n\n")
                        }
                        append("¡Ayuda! Esta es mi ubicación aproximada:\n")
                        append("Latitud: ${lat.format(6)}\n")
                        append("Longitud: ${lon.format(6)}\n\n")
                        append("Puedes verme en un mapa aquí:\n")
                        append("https://maps.google.com/?q=${lat.format(6)},${lon.format(6)}")
                    }
                    (context as? MainActivity)?.sendWhatsAppMessage(context, phoneNumber, completeMessage)
                } ?: run {
                    Toast.makeText(context, "Ubicación no disponible. Active el servicio GPS y espere.", Toast.LENGTH_LONG).show()
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppScreenColors.PrimaryAction,
                contentColor = AppScreenColors.PrimaryActionText
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(52.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Enviar Icono",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Enviar Ubicación por Whatsapp", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MaterialTheme {
        App(currentLocation = Location("preview").apply { latitude = 9.022905; longitude = -79.522996 })
    }
}