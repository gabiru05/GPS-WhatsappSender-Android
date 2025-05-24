package com.ar.backgroundlocation // Reemplaza con tu paquete

import android.Manifest
import android.content.ActivityNotFoundException // Asegúrate de tener este import
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.ar.backgroundlocation.ui.theme.BackGroundLocationTheme // Tu tema
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    private val TAG = MainActivity::class.java.simpleName

    // Registradores para los resultados de las solicitudes de permisos
    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var allPermissionsGranted = true
        permissions.entries.forEach { entry ->
            Log.d(TAG, "Permiso: ${entry.key}, Concedido: ${entry.value}")
            if (!entry.value) {
                allPermissionsGranted = false
            }
        }

        if (allPermissionsGranted) {
            Log.d(TAG, "Todos los permisos solicitados inicialmente fueron concedidos.")
            // Después de conceder los permisos iniciales, considera si necesitas ACCESS_BACKGROUND_LOCATION.
            // Usualmente, esto se pide en un contexto más específico, no inmediatamente.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && hasLocationPermission() && !hasBGLocationPermission()) {
                Log.d(TAG, "Solicitando permiso de ubicación en segundo plano...")
                requestBGLocationPerm.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                // Si ya tiene BG o es < Q, o si ya tiene todos los permisos,
                // puedes iniciar el servicio si es la lógica deseada
                // startLocationServiceIfNeeded()
            }
        } else {
            Log.e(TAG, "No todos los permisos esenciales fueron concedidos.")
            Toast.makeText(this, "Algunos permisos son necesarios para la funcionalidad completa.", Toast.LENGTH_LONG).show()
        }
    }

    // Este launcher parece ser para ACCESS_FINE_LOCATION específicamente,
    // si requestMultiplePermissions ya lo pide, podría ser redundante.
    // Lo mantengo por si tu flujo lo requiere así.
    private val requestLocationPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Permiso de ubicación fina CONCEDIDO (vía requestLocationPerm).")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasBGLocationPermission()) {
                requestBGLocationPerm.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        } else {
            Log.d(TAG, "Permiso de ubicación fina NO CONCEDIDO (vía requestLocationPerm).")
            Toast.makeText(this, "El permiso de ubicación fina es necesario.", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestBGLocationPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "Permiso de ubicación en segundo plano CONCEDIDO.")
        } else {
            Log.d(TAG, "Permiso de ubicación en segundo plano NO CONCEDIDO.")
            Toast.makeText(this, "Permiso de ubicación en segundo plano no concedido; algunas funciones pueden estar limitadas.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Solicitar permisos necesarios al iniciar la UI
            LaunchedEffect(key1 = Unit) {
                checkAndRequestNecessaryPermissions()
            }

            BackGroundLocationTheme { // Asegúrate que este sea el nombre correcto de tu tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Recolectar la ubicación desde el StateFlow del servicio
                    val location by LocationService.locationStateFlow.collectAsState()
                    App(
                        currentLocation = location
                        // Las funciones para iniciar/detener el servicio están dentro de App()
                        // y usan directamente el context para lanzar Intents.
                    )
                }
            }
        }
    }

    private fun checkAndRequestNecessaryPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (!hasLocationPermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        // Para Android 13 (API 33) y superior, se necesita el permiso de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPerm()) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissionsToRequest.isNotEmpty()) {
            Log.d(TAG, "Solicitando los siguientes permisos: ${permissionsToRequest.joinToString()}")
            requestMultiplePermissions.launch(permissionsToRequest.toTypedArray())
        } else {
            Log.d(TAG, "Permisos iniciales (Ubicación Fina y Notificaciones si aplica) ya están concedidos.")
            // Aquí podrías decidir si quieres pedir ACCESS_BACKGROUND_LOCATION
            // si ya tienes los otros y es versión Q+.
            // Por ahora, se maneja en el callback de requestMultiplePermissions.
        }
    }

    // --- Funciones Helper para verificar permisos ---
    private fun hasNotificationPerm(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // En versiones anteriores a Android 13, este permiso no se solicita explícitamente en tiempo de ejecución.
            true
        }
    }

    private fun hasLocationPermission(): Boolean {
        // Para esta app, ACCESS_FINE_LOCATION es el crucial.
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasBGLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Antes de Android Q, tener el permiso de ubicación fina implicaba acceso en segundo plano
            // si el servicio estaba correctamente configurado.
            true
        }
    }

    // --- Función para enviar mensajes por WhatsApp ---
    fun sendWhatsAppMessage(context: Context, phoneNumberWithCountryCode: String, message: String) {
        val tag = "WhatsAppSendFunc" // Tag para logs específico de esta función

        if (phoneNumberWithCountryCode.isBlank() || !phoneNumberWithCountryCode.startsWith("+") || phoneNumberWithCountryCode.length < 5) {
            Toast.makeText(context, "Número de teléfono inválido. Debe iniciar con '+' seguido del código de país y número.", Toast.LENGTH_LONG).show()
            Log.e(tag, "Número de teléfono inválido: $phoneNumberWithCountryCode")
            return
        }

        // Extrae solo los dígitos numéricos para el enlace wa.me (ej: de "+507123" a "507123")
        val finalPhoneNumberForWaMe = phoneNumberWithCountryCode.substring(1).filter { it.isDigit() }

        if (finalPhoneNumberForWaMe.isEmpty()) {
            Toast.makeText(context, "Formato de número de teléfono no reconocido después de limpiar.", Toast.LENGTH_LONG).show()
            Log.e(tag, "Número de teléfono vacío después de filtrar: $phoneNumberWithCountryCode -> $finalPhoneNumberForWaMe")
            return
        }

        try {
            val packageManager: PackageManager = context.packageManager
            val encodedMessage = URLEncoder.encode(message, "UTF-8")

            // Intento 1: Abrir con el paquete com.whatsapp
            val primaryUrl = "https://wa.me/$finalPhoneNumberForWaMe?text=$encodedMessage"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(primaryUrl)
                setPackage("com.whatsapp")
            }
            Log.d(tag, "Intentando abrir WhatsApp (com.whatsapp) con URL: $primaryUrl")

            if (intent.resolveActivity(packageManager) != null) {
                context.startActivity(intent)
                Log.i(tag, "Intent lanzado a com.whatsapp")
            } else {
                // Si com.whatsapp no resolvió, intentar con un intent genérico para el enlace web
                // Esto podría abrirlo en un navegador si el usuario tiene WhatsApp Web o una app que maneje el link.
                Log.d(tag, "com.whatsapp no encontrado o no resolvió. Intentando enlace web genérico (api.whatsapp.com).")
                val webUrl = "https://api.whatsapp.com/send?phone=$finalPhoneNumberForWaMe&text=$encodedMessage"
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                // No se fija paquete aquí para permitir que cualquier navegador lo maneje

                if (webIntent.resolveActivity(packageManager) != null) {
                    context.startActivity(webIntent)
                    Log.i(tag, "Intent lanzado a navegador web para WhatsApp: $webUrl")
                } else {
                    Log.e(tag, "No se encontró WhatsApp (com.whatsapp) ni un navegador web para manejar el link: $webUrl")
                    Toast.makeText(context, "No se encontró WhatsApp ni un navegador web para completar la acción.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: ActivityNotFoundException) {
            // Este catch es una doble seguridad, ya que resolveActivity debería prevenirlo.
            Log.e(tag, "ActivityNotFoundException: El sistema no encontró una actividad para manejar el intent.", e)
            Toast.makeText(context, "Error: Aplicación para manejar la acción no encontrada.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(tag, "Error general al intentar enviar mensaje por WhatsApp", e)
            Toast.makeText(context, "Error al enviar mensaje: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}