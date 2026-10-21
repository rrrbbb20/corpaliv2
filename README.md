

## Ejecutar en Android Studio

1. Descomprimir el ZIP. Abrir **solo** la carpeta `CORPALIV_AlbaLab_MVP` en Android Studio mediante **File > Open**.
2. Esperar **Gradle Sync**. El wrapper Gradle 8.13 está incluido, con AGP 8.12.3, Kotlin 2.0.21 y KSP 2.0.21-1.0.28 (versiones alineadas con ejemplos del docente). Se requiere acceso a repositorios Google/Maven la primera vez.
3. Instalar desde **SDK Manager** el SDK **Android API 36** si falta; usar **JDK 17 o 21** adecuado para el plugin de Android.
4. Ejecutar en emulador o teléfono Android **8.0/API 26 o superior**. Para demostrar cámara, lo más seguro es un **teléfono real** con una aplicación de cámara, porque algunos emuladores no traen una.
5. Crear una cuenta **ficticia** y recorrer las pantallas. Al cerrar y volver a abrir, ingresar de nuevo para ver que las cuentas y solicitudes permanecen en Room. La sesión no se mantiene abierta deliberadamente.
6. Para pruebas unitarias: **Run > Run 'ValidacionesTest'** o `./gradlew testDebugUnitTest`.

