# Race-Tracker-App
Aplicación que demuestra el uso de corrutinas en Jetpack Compose al utilizar ``LaunchedEffect()`` para llamar de forma segura las funciones de suspensión dentro de composables.

La aplicación utiliza dos corrutinas en el composable principal para iniciar dos contadores que representan los sendos progresos de participantes en una carrera.

![image](https://github.com/Camilo-Hernandez/Race-Tracker-App/assets/36543483/a714e07d-d787-418b-b1b8-8ad25f0052fc)

La aplicación utiliza ``launch()`` para proporcionar el contexto de las corrutinas (construirlas). También utiliza la función ``coroutineScope()`` para limitar el alcance de las corrutinas lanzadas en su interior.
De esta manera, se garantiza que se espera la completación de los Job secundarios del alcance antes de pasar a la siguiente línea del código síncrono. Con esto, la asincronía pasa a ser un mero detalle de implementación.
