# AgendaLarm — Mobile

App Android nativa (Kotlin + XML, Material 3). Es un proyecto Gradle completo: se abre en Android Studio con `File > Open` sobre `mobile/`.

Estado: solo el andamio (una pantalla vacía). Las pantallas reales se agregan encima.

## Requisitos

- Android Studio reciente, o JDK 17+ con el Android SDK (plataforma **36.1**).
- Celular con depuración USB activada, o un emulador.
- `mobile/local.properties` con `sdk.dir=/ruta/al/Android/Sdk` (no se versiona; Android Studio lo genera solo).

## Compilar e instalar

```
cd mobile
./gradlew assembleDebug    # APK en app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug     # compila e instala en el dispositivo conectado
```

## Estructura

```
app/src/main/
├── java/com/agendalarm/app/ui/
│   ├── base/BaseActivity.kt   # edge-to-edge + insets; toda pantalla hereda de aquí
│   └── main/MainActivity.kt   # pantalla vacía de andamio
└── res/
    ├── layout/                # XML con ConstraintLayout y data binding (<layout>)
    └── values/                # colors (mismos tokens que web/css/styles.css), themes, dimens, strings
```

## Convenciones

- Una pantalla = una Activity (extiende `BaseActivity`, llama a `aplicarInsets(binding.raiz)`) + un layout, en su paquete bajo `ui/`.
- Layouts con ConstraintLayout y `layout_constraintWidth_max="@dimen/ancho_max_contenido"` para no romperse en pantallas grandes.
- Sin colores, tamaños ni textos fijos en el layout: usar `@color/`, `@dimen/`, `@string/`.
- Declarar cada Activity nueva en `AndroidManifest.xml`. Un commit por pantalla.
