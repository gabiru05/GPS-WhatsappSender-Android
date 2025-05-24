package com.ar.backgroundlocation // Reemplaza con tu paquete

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.flow.MutableStateFlow // Nuevo import
import kotlinx.coroutines.flow.asStateFlow     // Nuevo import


class LocationService : Service(), LocationUpdatesCallBack {
    private val TAG = LocationService::class.java.simpleName

    private lateinit var gpsLocationClient: GPSLocationClient
    private var notification: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    // --- NUEVO: StateFlow para emitir la ubicación ---
    companion object {
        private val _locationStateFlow = MutableStateFlow<Location?>(null)
        val locationStateFlow = _locationStateFlow.asStateFlow() // Exponer como StateFlow inmutable

        // Mantener las constantes de acción
        const val ACTION_SERVICE_START = "ACTION_START" // Ya lo tenías
        const val ACTION_SERVICE_STOP = "ACTION_STOP"   // Ya lo tenías
    }
    // --- FIN DE LO NUEVO ---

    override fun onCreate() {
        super.onCreate()
        _locationStateFlow.value = null // Reiniciar al crear el servicio
        gpsLocationClient = GPSLocationClient()
        gpsLocationClient.setLocationUpdatesCallBack(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SERVICE_START -> startService()
            ACTION_SERVICE_STOP -> stopService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun startService() {
        gpsLocationClient.getLocationUpdates(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location Updates", // Nombre del canal más descriptivo
                NotificationManager.IMPORTANCE_LOW // Bajar importancia para que no sea tan intrusivo
            )
            val localNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            localNotificationManager.createNotificationChannel(channel)
            notificationManager = localNotificationManager // Asignar a la variable de clase
        }
        notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Servicio de Ubicación Activo") // Título más claro
            .setContentText("Obteniendo ubicación...")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Usa tu icono de app
            .setPriority(NotificationCompat.PRIORITY_LOW) // Bajar prioridad
            .setOngoing(true) // Para servicio en primer plano

        // Asignar notificationManager si no se hizo en el bloque OREO
        if (notificationManager == null && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        startForeground(1, notification?.build())
        Log.d(TAG, "Servicio de ubicación iniciado.")
    }

    private fun stopService() {
        Log.d(TAG, "Intentando detener el servicio de ubicación.")
        gpsLocationClient.setLocationUpdatesCallBack(null) // Detener actualizaciones del GPSLocationClient
        _locationStateFlow.value = null // Limpiar la última ubicación conocida
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf() // Detener el servicio
        Log.d(TAG, "Servicio de ubicación detenido.")
    }

    override fun locationException(message: String) {
        Log.d(TAG, "LocationException: $message")
        // Podrías emitir un estado de error aquí también si quieres mostrarlo en la UI
    }

    override fun onLocationUpdate(location: Location) {
        Log.d(TAG, "Nueva ubicación: (${location.latitude}, ${location.longitude})")
        // Actualizar el StateFlow para la UI
        _locationStateFlow.value = location

        // Actualizar la notificación (como ya lo hacías)
        val updatedNotification = notification?.setContentText(
            "Lat: ${location.latitude.format(6)}, Lon: ${location.longitude.format(6)}"
        )
        notificationManager?.notify(1, updatedNotification?.build())
    }

    // Función de utilidad para formatear decimales (puedes moverla a otro lado)
    private fun Double.format(digits: Int): String = "%.${digits}f".format(this)

    override fun onDestroy() {
        // Asegurarse de limpiar recursos si el servicio es destruido inesperadamente
        gpsLocationClient.setLocationUpdatesCallBack(null)
        _locationStateFlow.value = null
        super.onDestroy()
        Log.d(TAG, "LocationService onDestroy")
    }
}