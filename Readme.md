<h1 align="center">
  Android: Localizador GPS con Envío a WhatsApp
</h1>
<a name="readme-top"></a>

<h4 align="center">
  Aplicación Android para obtener la ubicación GPS actual y compartirla junto con un mensaje personalizado vía WhatsApp.<br>
  Desarrollada en Kotlin con Jetpack Compose.
</h4>

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">

#### <img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/accepted.gif" width= "30" alt="Icono verde de validación"> Para Fines Educativos y Demostración de Servicios de Localización y Comunicación entre Apps en Android

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">

<h2 align="center">Descripción del Proyecto</h2>

<p align="center">
  Este proyecto es una aplicación para Android desarrollada en Kotlin y Jetpack Compose que demuestra cómo obtener la ubicación GPS del dispositivo en tiempo real utilizando un servicio en primer plano (Foreground Service). La aplicación muestra las coordenadas de latitud y longitud en la interfaz y permite al usuario ingresar un número de teléfono (con prefijo de país) y un mensaje personalizado. Con un botón, se puede enviar toda esta información (mensaje personalizado + coordenadas GPS + un enlace a Google Maps) directamente a través de WhatsApp al número especificado. El proyecto se basa en el código de <a href="https://github.com/c85-pty/BackgroundLocation" target="_blank">BackgroundLocation</a> para la obtención de la ubicación y se extiende con la funcionalidad de envío por WhatsApp inspirada en <a href="https://github.com/c85-pty/SendMessageWhatsappCompose" target="_blank">SendMessageWhatsappCompose</a>.
</p>

<p align="center">
  <em>(Asegúrate de colocar tus capturas en una carpeta llamada "Screenshot" en la raíz de tu repositorio)</em><br>
  <img src="./Screenshot/ejemplo 8.png" width="250" alt="Captura de pantalla principal de la app">
  <img src="./Screenshot/ejemplo 6.png" width="250" alt="Captura de pantalla del mensaje enviado o notificación">
  <img src="./Screenshot/ejemplo 7.png" width="250" alt="Captura de pantalla mostrando detencion del servicio">
</p>

<h3 align="center">Creado/Adaptado por:</h3>
<p align="center">
  <a href="https://github.com/gabiru05">Gabriel Ruiz (gabiru05)</a>
  </p>

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">

<h2 align="center">Características Principales</h2>

<ul>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Obtención de Ubicación GPS:</strong> Utiliza un Foreground Service para rastrear la ubicación del dispositivo de manera eficiente.</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Visualización en Tiempo Real:</strong> Muestra las coordenadas de Latitud y Longitud actualizadas en la interfaz de usuario.</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Servicio Controlable:</strong> Botones para iniciar y detener manualmente el servicio de localización GPS.</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Mensajería Personalizada:</strong> Campos de texto para ingresar un número de teléfono de destino y un mensaje personalizado.</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Integración con WhatsApp:</strong> Envía el mensaje personalizado, las coordenadas GPS formateadas y un enlace de Google Maps a través de un intent de WhatsApp.</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Manejo de Permisos:</strong> Solicita los permisos necesarios en tiempo de ejecución (Ubicación, Notificaciones).</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Interfaz Moderna:</strong> Desarrollada con Jetpack Compose y Material 3 para una apariencia limpia y actual.</li>
  <li><img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/PNGs/BlueCheckCircleMark.png" width="20" alt="icono de característica"> <strong>Notificación de Servicio:</strong> Muestra una notificación persistente mientras el servicio de ubicación está activo.</li>
</ul>

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">

<h2 align="center">Estructura del Proyecto (Simplificada)</h2>

<p align="center">
  El proyecto sigue una estructura típica de una aplicación Android desarrollada con Kotlin y Jetpack Compose en Android Studio:
</p>

-   `app/`: Módulo principal de la aplicación.
    -   `src/main/`: Contiene el código fuente y los recursos.
        -   `java/com/ar/backgroundlocation/` (o tu paquete): Código Kotlin.
            -   `MainActivity.kt`: Actividad principal que maneja la lógica de permisos, observa los datos de ubicación y configura la UI de Compose.
            -   `MainScreen.kt`: Define el Composable `App()` que construye toda la interfaz de usuario.
            -   `LocationService.kt`: Servicio en primer plano encargado de obtener las actualizaciones de ubicación del GPS.
            -   `GPSLocationClient.kt`: Clase helper que interactúa con el FusedLocationProviderClient.
            -   `AppConstants.kt`: (Si existe) Constantes de la aplicación.
            -   `ui/theme/`: Carpeta generada por Android Studio para el tema de Compose (`Color.kt`, `Theme.kt`, `Type.kt`).
        -   `res/`: Carpeta de recursos (drawables, mipmap, values).
            -   `drawable/ic_person_placeholder.xml`: Icono vectorial para el placeholder de usuario.
        -   `AndroidManifest.xml`: Define permisos, componentes de la aplicación (incluyendo el `LocationService` y su `foregroundServiceType`), y las `<queries>` necesarias para interactuar con WhatsApp.
    -   `build.gradle.kts` (o `build.gradle`): Script de configuración de Gradle para el módulo `app`.
-   `gradle/libs.versions.toml` (si se usa Version Catalogs): Define las versiones de las dependencias y plugins.

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">

<h2 align="center">Tecnologías Utilizadas</h2>

<ul>
  <li><img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/kotlin/kotlin-original.svg" width="20" alt="Icono de Kotlin"> <strong>Kotlin:</strong> Lenguaje de programación principal.</li>
  <li><img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/jetpackcompose/jetpackcompose-original.svg" width="20" alt="Icono de Jetpack Compose"> <strong>Jetpack Compose:</strong> Toolkit moderno de Android para construir interfaces de usuario nativas.</li>
  <li><img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/android/android-plain.svg" width="20" alt="Icono de Android"> <strong>Android SDK:</strong> Para el desarrollo de aplicaciones nativas Android.</li>
  <li><strong>Servicios de Localización de Google Play:</strong> Para obtener la ubicación GPS de manera precisa y eficiente.</li>
  <li><strong>Foreground Service & Notifications:</strong> Para el seguimiento de ubicación y la comunicación con el usuario.</li>
  <li><strong>Material 3 Components:</strong> Para los elementos de la interfaz de usuario en Jetpack Compose.</li>
  <li><img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/androidstudio/androidstudio-original.svg" width="20" alt="Icono de Android Studio"> <strong>Android Studio:</strong> Entorno de Desarrollo Integrado (IDE) oficial.</li>
  <li><strong>Gradle & Version Catalogs:</strong> Para la gestión de dependencias y construcción del proyecto.</li>
</ul>

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">

<h2 align="center">Instalación y Uso</h2>

1.  **Clonar el repositorio:**
    ```bash
    git clone [URL_DE_TU_REPOSITORIO_AQUI]
    cd [NOMBRE_DE_LA_CARPETA_DEL_PROYECTO]
    ```
    *(Reemplaza con la URL de tu repositorio y el nombre de la carpeta)*

2.  **Abrir en Android Studio:**
    * Abre Android Studio.
    * Selecciona `File > Open...` o "Open an Existing Project".
    * Navega y selecciona la carpeta del proyecto clonado.
    * Espera a que Android Studio configure el proyecto y sincronice Gradle.

3.  **Configuración Previa en el Dispositivo/Emulador:**
    * **Habilita el GPS (Ubicación).**
    * **Instala WhatsApp** si deseas probar la funcionalidad de envío completa.

4.  **Ejecutar la aplicación:**
    * Conecta un dispositivo Android o inicia un Emulador.
    * Selecciona el dispositivo/emulador en Android Studio.
    * Haz clic en el botón "Run 'app'" (▶️).
    * **Otorga los permisos** de Ubicación y Notificaciones cuando la aplicación los solicite.

5.  **Cómo Usar:**
    * La aplicación mostrará "Push Notification Service" y las coordenadas Lat/Lon (inicialmente "---" hasta que se obtenga la primera ubicación).
    * Puedes usar los botones "Iniciar GPS" / "Detener GPS" para controlar el servicio de localización. El estado del servicio se indicará.
    * Verás el texto "Estoy perdido, por favor encuentrenme" y un icono de usuario.
    * Ingresa el número de teléfono de WhatsApp de destino en el campo "Número WhatsApp (+CódPaís)", asegurándote de incluir el código de país (ej: `+507...`).
    * Escribe un mensaje personalizado en el campo "Mensaje adicional (opcional)".
    * Presiona el botón "Enviar Ubicación por Whatsapp". Esto abrirá WhatsApp con el mensaje y la ubicación listos para enviar.

<img src="https://github.com/gabiru05/Gaby_Resource/blob/master/images/Gifs/1pxRainbowLine.gif" width= "300000" alt="línea horizontal RGB arcoíris súper delgada">
<p align="right"><a href="#readme-top">Volver arriba</a></p>
