# AgendaLarm — Mobile

App Android nativa (Kotlin + XML, Material 3). Es un proyecto Gradle completo: se abre en Android Studio con `File > Open` sobre `mobile/`.

Estado: base del proyecto lista (hoja de estilos, animaciones, tipografía y librerías) y **una pantalla terminada: M-04 · Alarmas del día**. Las demás se agregan encima, una por una. Sólo interfaz: no hay red, base de datos ni lógica de negocio; los datos son de muestra.

Fuente de diseño: `S6_Mockups_Movil_AgendaLarm.pdf` (15 pantallas de 390 × 844 dp, M-01 … M-15, y tres láminas del Design System: paleta, tipografía y componentes).

## Requisitos

- Android Studio reciente, o JDK 17+ con el Android SDK (plataforma **36.1**; Gradle la descarga sola si falta y las licencias están aceptadas).
- Celular con depuración USB activada, o un emulador.
- `mobile/local.properties` con `sdk.dir=/ruta/al/Android/Sdk` (no se versiona; Android Studio lo genera solo, o basta con `ANDROID_HOME`).

## Compilar e instalar

```
cd mobile
./gradlew assembleDebug    # APK en app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug     # compila e instala en el dispositivo conectado
./gradlew lintDebug        # análisis estático (debe terminar sin errores)
```

### Correr la app en local (en orden)

```
# 1. Emuladores disponibles y arranque (agregar -no-window para no abrir ventana)
$ANDROID_HOME/emulator/emulator -list-avds
$ANDROID_HOME/emulator/emulator -avd Medium_Phone_API_36 &

# 2. Esperar a que Android termine de arrancar
adb wait-for-device
until [ "$(adb shell getprop sys.boot_completed | tr -d '\r')" = 1 ]; do sleep 2; done

# 3. Compilar e instalar
cd mobile && ./gradlew installDebug

# 4. Abrir M-04 (o tocar el ícono del reloj en el lanzador)
adb shell am start -n com.agendalarm.app/.ui.alarmasdia.M04AlarmasDelDiaActivity

# 5. Al terminar
adb emu kill
```

Con Android Studio: abrir `mobile/`, elegir el emulador o el celular y pulsar Run.

## Estructura

```
app/
├── lint.xml                                # la paleta y las hojas de estilo son base aún sin usar: no avisar
└── src/main/
    ├── AndroidManifest.xml
    ├── assets/licencias/                   # Inter-OFL.txt (tipografía) y Noto-Emoji-OFL.txt (ícono)
    ├── java/com/agendalarm/app/ui/
    │   ├── base/BaseActivity.kt            # edge-to-edge + insets; toda pantalla hereda de aquí
    │   ├── animacion/Animaciones.kt        # conTransicion(), animaciones de lista (espejo de main.js)
    │   └── alarmasdia/                     # M-04: Activity, ViewModel, estado, adaptador, datos de muestra
    └── res/
        ├── layout/       activity_m04_alarmas_del_dia · include_encabezado_pantalla · item_alarma · item_seccion · item_nota
        ├── values/       colors_paleta · colors · dimens · styles_texto · styles_componentes · themes · animaciones · strings
        ├── color/        sel_*: colores por estado (reposo / pulsado / deshabilitado) de botones
        ├── font/         Inter Regular, Medium, SemiBold y Bold, más inter.xml (la familia completa)
        ├── drawable/     fondo_z3 · interruptor_pista · interruptor_pulgar · campo_fondo
        ├── drawable-nodpi/ ic_launcher_foreground.png
        ├── mipmap-anydpi/  ic_launcher.xml (ícono adaptable)
        ├── anim/         transicion_entrar · transicion_salir · transicion_quedar
        ├── interpolator/ ease_in_out
        └── transition/   transicion_estandar
```

## Convención de nombres (equivalencias con web)

| Web | Android |
| --- | --- |
| `W-05-panel-principal.html` | `activity_m04_alarmas_del_dia.xml` + `M04AlarmasDelDiaActivity.kt` (código del mockup + nombre) |
| `css/styles.css`, variables `--color-*` de `:root` | `values/colors.xml` (tokens semánticos) sobre `values/colors_paleta.xml` (8 roles × 12 pasos) |
| Clases BEM (`.tarjeta-alarma`, `.boton--primario`) | Estilos `Widget.AgendaLarm.Tarjeta`, `Widget.AgendaLarm.Boton.Primario`, `TextAppearance.AgendaLarm.*` |
| `@view-transition` 220 ms ease-in-out | Tema `Animacion.AgendaLarm.Ventana` + `res/anim/transicion_*`; duración en `@integer/duracion_transicion` |
| `conTransicion(cambio)` en `main.js` | `ViewGroup.conTransicion { }` en `Animaciones.kt` |
| `activarNavegacion()`, `activarModales()` … | `activarLista()` … : una función `activar<Interacción>()` por comportamiento |
| ids `lista-compromisos`, `boton-subir` | ids `lista_alarmas`, `interruptor` (snake_case) |
| `assets/fondo-z3.svg` | `drawable/fondo_z3.xml` (mismo arte, convertido a VectorDrawable) |

Todo en español y en minúsculas; los layouts, ids y recursos llevan `snake_case`, las clases `PascalCase` y las funciones `camelCase`.

## Design System

**Color.** `colors_paleta.xml` guarda los 96 pasos de la lámina «06 · Paleta de colores» (primario, secundario, acento, neutros, error, confirmación, atención, informativo; 990 → 25, el 500 es la base). Las pantallas no los usan directamente: pasan por los tokens de `colors.xml` (`superficie`, `tarjeta`, `texto_primario`, `texto_secundario`, `borde_tarjeta`, `boton`, `exito`…), igual que web usa variables CSS. Los tokens móviles salen de esa paleta, por lo que algunos difieren algo de los muestreados a ojo en web (p. ej. botón `#8E4F19` = acento 900). Los pocos colores sin paso equivalente (pista y borde del interruptor apagado) están muestreados del mockup y marcados como tales.

**Tipografía.** Inter (SIL OFL), empaquetada en `res/font/`. La lámina «07 · Tipografía» trae tamaños «pt» de otra escala, así que la escala real se midió sobre los mockups:

| Estilo | sp · peso | Uso |
| --- | --- | --- |
| `TituloApp` | 28 · SemiBold | «AgendaLarm», «Mañana» |
| `Titulo` / `Subtitulo` | 24 / 22 · SemiBold | estado o pantalla / sección |
| `Encabezado` | 18 · SemiBold | título de la franja superior |
| `Seccion` | 15 · SemiBold | «Hoy, martes 12 · 4 compromisos» |
| `TarjetaTitulo` | 14 · SemiBold | título de una tarjeta |
| `CuerpoGrande` / `Cuerpo` | 16 / 14 · Regular | párrafos |
| `Etiqueta` / `EtiquetaDestacada` | 13 · Regular / Medium | etiquetas de campos |
| `Detalle` | 12 · Regular | detalle de tarjeta, nota al pie |
| `Micro` | 11 · Regular | acceso y código del encabezado |
| `Boton` | 14 · SemiBold | botones |
| `Reloj`, `ValorGrande`, `Valor` | 66 · SemiBold, 34 · Bold, 24 · SemiBold | cifras de M-10 y M-06 |

Cada `TextAppearance` trae su propio peso. **No** fijar `android:fontFamily` en el tema: a ese nivel pisa el de los `TextAppearance` y todo sale en Regular.

**Componentes** (`styles_componentes.xml`). Se aplican solos por el tema (`materialButtonStyle`, `materialCardViewStyle`, `switchStyle`, `editTextStyle`, `textViewStyle`).
- Verificados en pantalla contra el mockup: `Tarjeta` (borde 1 dp, radio 12 dp) e `Interruptor` (pista 44 × 24 dp, pulgar 18 dp) en M-04; `Boton.Primario` (48 dp, radio 10 dp, relleno acento 900, borde primario 990), `Boton.Destructivo` y `Campo` (52 dp, sangría 16 dp, placeholder 14 sp) comparados con los mockups del PDF en una pantalla de prueba ya retirada: tamaños, colores de relleno y posición del texto coinciden.
- Por verificar al construir su pantalla: `Boton.Secundario`, el estado pulsado y el borde del campo enfocado. El estado deshabilitado se vio en pantalla (fondo gris claro, texto atenuado).

**Ícono de la app.** El mismo emoji del reloj despertador que las 8 pantallas de web usan de favicon (`<text>⏰</text>`), en su versión Noto Color Emoji de Google (licencia en `assets/licencias/Noto-Emoji-OFL.txt`). Es un ícono adaptable: primer plano PNG de 432 × 432 px con el dibujo dentro de la zona segura de 66 dp, sobre el color `superficie`. Con `minSdk 26` no hacen falta PNG heredados. La pantalla de arranque de Android 12+ lo reutiliza.

**Interacción.** Los botones y campos responden al toque como en web —estado pulsado (un paso más oscuro), foco, deshabilitado, transición al mostrar cosas— pero **no validan ni guardan datos**: lo que cambia vive en memoria (ViewModel) y se pierde al cerrar la app. En M-04 lo interactivo son los interruptores; la pantalla no tiene botones ni campos de texto.

**Movimiento.** Una sola curva y una sola duración para todo, como en web (Figma: smart animate, ease in and out, 220 ms):
- Entre pantallas: fundido cruzado definido en el tema; no requiere código al llamar `startActivity`. Comprobado en el emulador con las animaciones del sistema al ×10: apertura (la nueva pantalla se funde encima) y cierre (se desvanece dejando ver la anterior) sin oscurecerse en medio.
- Dentro de una pantalla: `contenedor.conTransicion { … }` anima mostrar/ocultar y agregar/quitar vistas.
- Listas: `recycler.usarAnimacionesAgendaLarm()`.
- Interruptor: la pista se funde en 150 ms (`@integer/duracion_interruptor`).

**Librerías** (`gradle/libs.versions.toml`): AndroidX Core, AppCompat, Material 3, ConstraintLayout, RecyclerView, Transition, Activity KTX, Lifecycle (ViewModel + LiveData) y, para pruebas, JUnit, AndroidX Test y Espresso. Data binding y view binding activados. Si se agregan diálogos, menús o snackbars, mapear los `textAppearance*` del tema a Inter (ver `styles_texto.xml`).

## Pantallas

- `M04AlarmasDelDiaActivity` — Sebastián Vega

### M-04 · Alarmas del día

Pantalla principal de consulta diaria: encabezado con fecha y número de compromisos, y una tarjeta por alarma con el compromiso que la origina y su interruptor.

- Reproduce el mockup completo: franja blanca de 70 dp con título, tipo de acceso («Requiere autenticación») y código «M4», tal como están dibujados en el PDF; tarjetas de 350 × 78 dp; interruptores; nota final; y el fondo decorativo `fondo_z3` (405 × 301 dp, detrás de la lista).
- `fondo_z3` es el mismo arte que `web/assets/fondo-z3.svg`, convertido a VectorDrawable. En los mockups móviles se ve ≈ 25 % más opaco que en web, así que la conversión multiplica todas sus opacidades por 1,25; la escala (405 × 301 dp, algo estirada en vertical) y la posición (2 dp del borde, 208 dp bajo la franja) se ajustaron por correlación contra M-04 y M-05.
- La franja se extiende bajo la barra de estado (`BaseActivity.aplicarInsets(encabezado, contenido)`); la lista se desplaza por debajo de la barra de navegación.
- Título, tarjetas y nota son **una sola lista** (`RecyclerView` con `ListAdapter` + DiffUtil y tres tipos de fila), así que se desplazan juntos.
- Interacción: cada interruptor cambia su alarma en el `AlarmasDelDiaViewModel` (LiveData); el estado sobrevive a rotaciones. Nada se guarda: al cerrar la app vuelve a los datos de muestra (`AlarmasMuestra.kt`).
- Las tarjetas aún no navegan: el detalle (M-06) no existe todavía. Igual que en web, sólo se enlaza lo que ya está construido.
- Es la actividad de arranque de la app. No se construyen el inicio de sesión (M-01) ni la vinculación del dispositivo (M-02 y M-03): no fueron solicitados.
- El estado vacío (M-05) se agregará como otro estado de esta misma pantalla.
- Accesibilidad: título y sección son encabezados para TalkBack; cada interruptor tiene descripción («Alarma Despertar») y una zona táctil de 60 × 48 dp aunque se dibuja de 44 × 24 dp; la ventana anuncia su título; el texto respeta el tamaño de fuente del sistema (probado al 130 %).

**Verificación contra el mockup.** Emulador con `wm size 1170x2532` y `wm density 480` (= 390 × 844 dp exactos); captura reescalada a 780 × 1688 (la escala del PDF) y alineada por la altura de la barra de estado. Se midieron cajas de texto, bordes, interruptores y colores planos con el mismo método sobre las dos imágenes:

| Elemento | Diferencia app / mockup |
| --- | --- |
| Título, acceso, sección, títulos y detalles de tarjeta (posición) | ≤ 0,5 dp |
| Código «M4» | 1 dp en horizontal |
| Bordes de tarjeta y separación entre tarjetas | ≤ 0,5 dp |
| Pista y posición de los interruptores | ≤ 0,5 dp |
| Colores de fondo, franja y tarjeta | idénticos |
| Ancho de los textos | +1 dp en títulos, +3 dp en la nota (315 dp): Inter 4.0 mide un poco distinto que la versión usada en Figma |
| Fondo decorativo | alineado a ≤ 0,5 dp; intensidad dentro de ±20 % |

Repetir tras cambiar estilos: compilar, instalar en un emulador con ese tamaño y densidad, capturar (`adb exec-out screencap -p`) y comparar contra la figura extraída del PDF (`pdfimages -png -f 7`).

**Decisiones y avisos de diseño**
- El código «M4» y «Requiere autenticación» son anotaciones de documentación del PDF; se reprodujeron porque el objetivo es la fidelidad. Están en `strings.xml` (`m04_*`) y en un único `include`, así que quitarlos es una línea por pantalla.
- La lámina de componentes dice que la barra de navegación inferior está «presente en todas las pantallas autenticadas», pero M-04 no la dibuja; se siguió el mockup.
- Botón destructivo (M-06): texto blanco sobre `error 200` da un contraste de ≈ 2,1 : 1, por debajo del mínimo de WCAG. Se conservará como en el mockup salvo que el equipo decida ajustarlo.

**Pendiente de la base**
- Por verificar en un dispositivo, en la primera pantalla que los use: estado pulsado de los botones y foco del campo.
- `android:dataExtractionRules` (aviso de lint heredado del andamio, junto con `allowBackup="false"`).

## Convenciones

- Una pantalla = una Activity (extiende `BaseActivity`) + un layout, en su paquete bajo `ui/`. El nombre lleva el código del mockup: `M04AlarmasDelDiaActivity` ↔ `activity_m04_alarmas_del_dia.xml`; ViewModel, adaptador y estado usan el nombre sin código.
- Encabezado de cada pantalla: `<include layout="@layout/include_encabezado_pantalla" …>` con `titulo`, `acceso` y `codigo`; luego `aplicarInsets(encabezado, contenido)`. Las pantallas sin franja usan `aplicarInsets(raiz)`; ambas suman los insets al relleno del layout.
- Botones y campos: interactivos como en web (pulsado, foco, transición) sin validar ni guardar; si una pantalla necesita conservar algo mientras está abierta, va en su ViewModel.
- Patrón MVVM: la vista sólo dibuja un estado (`…UiState`) que expone el ViewModel; los eventos suben con funciones `alternarAlarma(…)`.
- Sin colores, tamaños ni textos fijos en el layout: usar `@color/` (tokens), `@dimen/`, `@string/` y los estilos de la hoja. Si un valor nuevo sale de un mockup, se agrega a `dimens.xml` con un comentario.
- Layouts con ConstraintLayout y `layout_constraintWidth_max="@dimen/ancho_max_contenido"` para no romperse en pantallas grandes.
- `SwitchCompat` mide la altura de su texto vacío y centra la pista ahí: cualquier interruptor nuevo necesita el `minHeight` que ya trae `Widget.AgendaLarm.Interruptor`.
- Los comentarios XML no pueden contener dos guiones seguidos.
- Declarar cada Activity nueva en `AndroidManifest.xml`. Un commit por pantalla.
