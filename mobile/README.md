# AgendaLarm — Móvil

Aplicación Android nativa de **AgendaLarm · Servicio de gestión de alarmas**, escrita en Kotlin con layouts XML y Material 3. Reúne las pantallas de uso diario de las alarmas: consulta del día (con o sin compromisos), resumen de mañana, detalle de una alarma, elección del método para confirmar el despertar y confirmación de pie con barra de progreso.

## Datos del proyecto

| | |
| --- | --- |
| **Proyecto** | AgendaLarm · Servicio de gestión de alarmas |
| **Universidad** | Universidad de los Andes |
| **Programa** | MISO · Maestría en Ingeniería de Software |
| **Curso** | Diseño de Experiencia de Usuario para el Desarrollo de Software |
| **Docente** | Sergio Acosta |
| **Integrantes** | Juan Sebastián Vega Guarín · Julio César Urian Villamil |
| **Entregable** | Mockups de aplicación móvil y componentes del Design System |
| **Herramienta de diseño** | Figma |
| **Ciudad y fecha** | Bogotá D.C., septiembre de 2026 |

## Descripción

AgendaLarm está pensada para quienes estudian y trabajan al mismo tiempo. Cada alarma se origina en un compromiso del horario de la persona, así que la aplicación muestra qué compromiso la origina, dónde es, a qué hora debe salir y con cuánto margen, y a la hora programada pide confirmar el despertar levantándose o registrando una fotografía.

Las pantallas reproducen los mockups de `S6_Mockups_Movil_AgendaLarm.pdf` (390 × 844 dp) y comparten el Design System del proyecto: paleta de ocho roles, tipografía Inter, componentes y movimiento. Es un prototipo de interfaz: trabaja con datos de muestra en memoria y no se conecta a servicios externos ni a una base de datos.

## Pantallas

| Código | Pantalla | Clase | Acceso | Responsable |
| --- | --- | --- | --- | --- |
| M-04 | Alarmas del día | `M04AlarmasDelDiaActivity` | Requiere autenticación | Juan Sebastián Vega Guarín |
| M-05 | Alarmas del día · estado vacío | `M05AlarmasDiaVacioActivity` | Estado vacío | Juan Sebastián Vega Guarín |
| M-06 | Detalle de la alarma | `M06DetalleAlarmaActivity` | Requiere autenticación | Juan Sebastián Vega Guarín |
| M-07 | Resumen de mañana | `M07ResumenMananaActivity` | Requiere autenticación | Juan Sebastián Vega Guarín |
| M-09 | ¿Cómo quieres confirmar que despertaste? | `M09MetodoConfirmacionActivity` | Requiere autenticación | Juan Sebastián Vega Guarín |
| M-11 | Levántate para apagar la alarma | `M11LevantateApagarActivity` | Requiere autenticación | Juan Sebastián Vega Guarín |
| M-12 | Alarma cumplida | `M12AlarmaCumplidaActivity` | Estado del sistema | Julio Urian |

Cada pantalla lleva en su franja superior el nombre, el tipo de acceso y el código del mockup (por ejemplo «M11»), tal como aparece en el documento de diseño.

**Flujo de navegación:** M-05 (pantalla de arranque) → toca la mitad derecha de la pantalla («día siguiente») → M-07 → botón «Ver alarmas» → M-04 → toca una tarjeta → M-06 → botón «Editar Alarma» → M-09 → elige «Ponerte de pie y sostener el teléfono» → M-11 (Atrás va devolviendo: M-11 a M-09, M-09 a M-06 y M-06 a M-04). La mitad izquierda de M-05 («día anterior») no navega: el mockup no tiene una pantalla para ese caso. Con esto, las seis pantallas quedan enlazadas entre sí.

### M-04 · Alarmas del día

Pantalla principal de consulta diaria, con compromisos para el día. El encabezado indica la fecha y el número de compromisos («Hoy, martes 12 · 4 compromisos»). Debajo hay una tarjeta por alarma con el compromiso que la origina (título, hora y lugar) y un interruptor para activarla o desactivarla, y al final la nota «Cada alarma se origina en un compromiso de tu horario.».

- Alarmas de muestra: *Despertar* (07:00), *Clase Bases de Datos* (08:30, Universidad), *Reunión equipo* (11:00, Trabajo) y *Tutoría* (15:30, Universidad, desactivada).
- Se abre desde el botón «Ver alarmas» de M-07. Tocar una tarjeta abre el detalle de esa alarma (M-06); tocar el interruptor sólo cambia el estado de la alarma.
- Título, tarjetas y nota forman una sola lista que se desplaza junta. El estado de los interruptores se conserva al rotar el dispositivo y al volver desde M-06.

### M-05 · Alarmas del día · estado vacío

Pantalla de arranque de la aplicación: mismo encabezado que M-04 pero sin compromisos para el día consultado. Muestra una ilustración de una cama y los mensajes «No tienes compromisos hoy.» y «¡Buen descanso!».

- Dos zonas táctiles invisibles a los lados del contenido cambian de día: la izquierda («día anterior») no navega, el mockup no tiene una pantalla para ese caso; la derecha («día siguiente») abre M-07.
- Es la única `Activity` exportada como `LAUNCHER` en el manifiesto; M-04 dejó de ser la pantalla de arranque al construir esta pantalla.

### M-06 · Detalle de la alarma

Información completa del compromiso que origina una alarma: ilustración de una campana, nombre del compromiso, hora, lugar, *Hora de salida estimada* y *Margen calculado*, con las acciones **Editar Alarma** y **Eliminar Alarma**.

- Se abre al tocar una tarjeta de M-04; cada una de las cuatro alarmas muestra su propio detalle, y Atrás vuelve a M-04. El botón «Editar Alarma» abre M-09; «Eliminar Alarma» sigue sin acción en los mockups.
- El identificador de la alarma viaja en el `Intent` y el ViewModel lo lee de `SavedStateHandle`, de modo que la pantalla se restaura igual tras rotar o si el sistema cierra el proceso.
- El contenido está en un `NestedScrollView`: se desplaza en pantallas bajas, en horizontal o con texto grande.

### M-07 · Resumen de mañana

Adelanto de los compromisos del día siguiente: una lista de tarjetas (título, hora y lugar de cada compromiso) y, debajo, un botón con la cantidad programada («3 compromisos programados»).

- Se abre al tocar la mitad derecha de M-05 («día siguiente»); el botón «Ver alarmas» lleva a M-04.
- Compromisos de muestra: *Clase Bases de Datos* (08:30, Universidad), *Reunión equipo* (11:00, Trabajo) y *Tutoría* (15:30, Universidad).
- Primera pantalla que separa los datos de muestra detrás de una interfaz (`ResumenMananaRepositorio` / `ResumenMananaRepositorioMuestra`), sustituible por una fuente real sin tocar el ViewModel.

### M-09 · ¿Cómo quieres confirmar que despertaste?

Selección del método con el que se confirma el despertar: **Ponerte de pie y sostener el teléfono** o **Registro fotográfico**. Cada método es una tarjeta con su interruptor; el método activo muestra el indicador «Activo».

- Se abre al tocar «Editar Alarma» en M-06; Atrás vuelve a M-06.
- Sólo un método puede estar activo: tocar una tarjeta o encender su interruptor la activa y desactiva la otra. Siempre hay un método elegido, y la pantalla arranca con «Ponerte de pie y sostener el teléfono».
- Las dos tarjetas tienen una variante activa y otra en reposo (posición del icono y del título) y, al cambiar de método, sólo se anima la tarjeta que cambia.
- Elegir «Ponerte de pie y sostener el teléfono» (tocar su tarjeta o encender su interruptor) abre M-11; elegir «Registro fotográfico» sólo cambia el método activo, no tiene pantalla propia.

### M-11 · Levántate para apagar la alarma

Confirmación del despertar mediante un movimiento sostenido: la pantalla pide «Mantente de pie unos segundos para confirmar que despertaste», ilustra el gesto con una persona caminando y muestra el avance con una barra de progreso y su porcentaje (50 % en el prototipo).

- Se abre al elegir «Ponerte de pie y sostener el teléfono» en M-09; Atrás vuelve a M-09.
- La barra es un `ProgressBar` con extremos redondos; el relleno se dibuja dentro de un `<scale>` para que conserve su forma redondeada por ambos lados. La barra y el texto «50%» salen del mismo valor del estado (`LevantateApagarUiState.progreso`, de 0 a 100).
- La ilustración es un `VectorDrawable` propio (`ilustracion_gesto.xml`: cuerpo, cabeza y cuatro rayas de velocidad).
- El contenido (ilustración, instrucción, barra y porcentaje) ocupa una columna de 342 dp de ancho, tal como está en el mockup, y se desplaza si no cabe en pantalla.

### M-12 · Alarma cumplida

Estado del sistema que confirma el despertar: marca de confirmación verde, el mensaje «¡Buen día! Alarma cumplida» y la hora efectivamente registrada: la del reloj del dispositivo en el momento en que se abre la pantalla.

- La marca es un `VectorDrawable` propio (`marca_confirmacion.xml`: aro, franja blanca, disco y visto).
- La hora sale del estado (`AlarmaCumplidaUiState.horaRegistrada`, en formato «HH:mm» de 24 horas); se toma una sola vez, así que no cambia al rotar. TalkBack la anuncia como «Despertar registrado a las 07:03» (con la hora que corresponda).

## Tecnologías y versiones

| Tecnología | Versión |
| --- | --- |
| Kotlin | 2.2.10 |
| Java (JDK y nivel de lenguaje) | JDK 17 · nivel de lenguaje 11 |
| XML | Layouts, recursos y data binding de Android |
| Gradle (scripts en Kotlin DSL) | 9.3.1 |
| Android Gradle Plugin | 9.1.0 |
| Android SDK | compileSdk 36.1 · targetSdk 36 · minSdk 26 (Android 8.0) |
| Material Components | 1.13.0 (Material 3) |
| AndroidX | Core KTX 1.18.0 · AppCompat 1.7.1 · ConstraintLayout 2.2.1 · RecyclerView 1.4.0 · Transition 1.6.0 · Activity KTX 1.13.0 · Lifecycle 2.10.0 (ViewModel, SavedState, LiveData) |
| Pruebas | JUnit 4.13.2 · AndroidX Test Ext JUnit 1.3.0 · Espresso 3.7.0 |
| Tipografía | Inter 4.000 (SIL OFL) |
| Ícono | Emoji ⏰ de Noto Color Emoji (SIL OFL) |

Las versiones están centralizadas en `gradle/libs.versions.toml`.

## Estructura de carpetas

```
mobile/
├── README.md
├── build.gradle.kts                          # plugins del proyecto
├── settings.gradle.kts                       # módulo :app y repositorios
├── gradle.properties
├── gradlew · gradlew.bat                     # wrapper de Gradle (Linux/macOS y Windows)
├── gradle/
│   ├── libs.versions.toml                    # catálogo de versiones de librerías y plugins
│   └── wrapper/                              # gradle-wrapper.jar y gradle-wrapper.properties
└── app/
    ├── build.gradle.kts                      # SDK, data binding y dependencias del módulo
    ├── lint.xml · proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── assets/licencias/             # Inter-OFL.txt · Noto-Emoji-OFL.txt
        │   ├── java/com/agendalarm/app/ui/
        │   │   ├── base/                     # BaseActivity: modo borde a borde y márgenes de las barras del sistema
        │   │   ├── animacion/                # Animaciones.kt: transición estándar y animaciones de lista
        │   │   ├── alarmasdia/               # M-04: Activity, ViewModel, UiState, Adapter, datos de muestra
        │   │   ├── alarmasdiavacio/          # M-05: Activity, ViewModel, UiState (pantalla de arranque)
        │   │   ├── detallealarma/            # M-06: Activity, ViewModel, UiState, datos de muestra
        │   │   ├── resumenmanana/            # M-07: Activity, ViewModel, UiState, Adapter, repositorio de muestra
        │   │   ├── metodoconfirmacion/       # M-09: Activity, ViewModel, UiState, Adapter
        │   │   ├── levantateapagar/          # M-11: Activity, ViewModel, UiState
        │   │   └── alarmacumplida/           # M-12: Activity, ViewModel, UiState
        │   └── res/
        │       ├── layout/                   # activity_m04… · activity_m05… · activity_m06… · activity_m07…
        │       │                             # activity_m09… · activity_m11… · activity_m12…
        │       │                             # include_encabezado_pantalla · include_dato_etiquetado
        │       │                             # item_alarma · item_compromiso · item_seccion · item_nota
        │       │                             # item_introduccion · item_opcion_metodo
        │       ├── values/                   # colors_paleta · colors · dimens · styles_texto · styles_componentes
        │       │                             # themes · animaciones · strings
        │       ├── drawable/                 # fondo_z3 · ilustracion_cama · ilustracion_campana · ilustracion_gesto
        │       │                             # marca_confirmacion · barra_progreso · ic_mas_opciones · punto_lugar
        │       │                             # icono_metodo · interruptor_pista · interruptor_pulgar · campo_fondo
        │       ├── drawable-nodpi/           # ic_launcher_foreground.png
        │       ├── mipmap-anydpi/            # ic_launcher.xml (ícono adaptable)
        │       ├── color/                    # colores por estado de los botones (sel_*)
        │       ├── font/                     # Inter Regular, Medium, SemiBold, Bold e inter.xml
        │       ├── anim/ · interpolator/ · transition/   # movimiento entre pantallas y dentro de ellas
        └── test/java/com/agendalarm/app/ui/  # pruebas unitarias: detallealarma · metodoconfirmacion · levantateapagar · alarmacumplida
```

## Arquitectura y convenciones

- **Una pantalla = una `Activity` + un layout + un ViewModel**, en su paquete bajo `ui/`. Toda `Activity` extiende `BaseActivity`, que activa el modo borde a borde y suma los márgenes de las barras del sistema.
- **MVVM.** El ViewModel expone un estado inmutable (`…UiState`) en un `LiveData`; la vista sólo lo dibuja mediante data binding y los eventos suben al ViewModel con funciones como `alternarAlarma(…)` o `seleccionar(…)`. Las reglas (por ejemplo «un solo método activo» o «el avance va de 0 a 100») viven en el estado y se prueban en la JVM, sin emulador.
- **Listas.** M-04, M-07 y M-09 usan `RecyclerView` con `ListAdapter` y `DiffUtil`; las animaciones de altas, bajas y cambios duran lo mismo que el resto de la app.
- **Datos entre pantallas.** Una pantalla que recibe un dato lo recibe en el `Intent`, con una fábrica en su `Activity` (`M06DetalleAlarmaActivity.intent(contexto, id)`), y su ViewModel lo lee de `SavedStateHandle`.
- **Datos de muestra.** M-07 es la primera pantalla que los separa detrás de una interfaz (`ResumenMananaRepositorio` / `ResumenMananaRepositorioMuestra`), sustituible por una fuente real sin tocar el ViewModel; las demás los tienen en un objeto directo (`AlarmasMuestra`, `DetalleAlarmaMuestra`).
- **Recursos sin valores fijos.** Los layouts usan `@color/` (tokens), `@dimen/`, `@string/` y los estilos de la hoja; las medidas salen de los mockups y llevan un comentario en `dimens.xml`.
- **Nombres.** Todo en español; layouts, ids y recursos en `snake_case`, clases en `PascalCase` y funciones en `camelCase`. El nombre de cada pantalla lleva el código del mockup: `M04AlarmasDelDiaActivity` ↔ `activity_m04_alarmas_del_dia.xml`.
- **Accesibilidad.** Los títulos son encabezados para TalkBack, los interruptores se anuncian con su nombre y tienen una zona táctil de 60 × 48 dp, las imágenes decorativas se ocultan a los lectores de pantalla y el texto respeta el tamaño de fuente del sistema.
- **Adaptación.** El contenido se limita a 420 dp de ancho y se centra en pantallas grandes o en horizontal.

## Design System

Los estilos viven en `app/src/main/res/values/` y se aplican solos por el tema.

- **Color.** `colors_paleta.xml` guarda la paleta de ocho roles (primario, secundario, acento, neutros, error, confirmación, atención, informativo) en 12 pasos cada uno (990 a 25). Las pantallas usan los tokens semánticos de `colors.xml` (`superficie`, `tarjeta`, `texto_primario`, `boton`, `exito`…).
- **Tipografía.** Inter, con un `TextAppearance` por uso:

| Estilo | Tamaño · peso | Uso |
| --- | --- | --- |
| `TituloApp` · `Titulo` · `Subtitulo` | 28 · 24 · 22 sp, SemiBold | Títulos |
| `Encabezado` | 18 sp, SemiBold | Título de la franja superior |
| `Seccion` · `TarjetaTitulo` | 15 · 14 sp, SemiBold | Sección y título de tarjeta |
| `CuerpoGrande` · `Cuerpo` · `CuerpoMarca` | 16 · 14 · 15 sp, Regular | Párrafos, mensaje secundario (M-05), lugar del compromiso (M-06) e instrucción (M-11) |
| `OpcionTitulo` · `EstadoActivo` | 16 sp SemiBold · 22 sp Medium | Selector de opción (M-09) |
| `Etiqueta` · `EtiquetaDestacada` · `EtiquetaDato` | 13 sp | Etiquetas de datos y campos |
| `Progreso` | 13 sp, Regular | Porcentaje bajo la barra (M-11) |
| `Registro` | 19 sp, Regular | Hora registrada (M-12) |
| `Detalle` · `Micro` | 12 · 11 sp, Regular | Notas, acceso y código de la franja |
| `Boton` | 14 sp, SemiBold | Botones |
| `Reloj` · `ValorGrande` · `Valor` | 66 · 34 · 24 sp | Cifras |

- **Componentes.** Botón primario, secundario y destructivo (48 dp, radio 10 dp), tarjeta (borde de 1 dp, radio 12 dp), interruptor (pista de 44 × 24 dp), campo de texto (52 dp), selector de opción, bloque de datos con etiqueta, barra de progreso (16 dp) y el encabezado de pantalla de 70 dp. Los botones y campos responden al toque con estado pulsado, foco y deshabilitado.
- **Movimiento.** Una sola curva (*ease in-out*) y una sola duración (220 ms) para el cambio de pantalla y para los cambios dentro de una pantalla; el interruptor anima su pista en 150 ms.
- **Fondo y recursos gráficos.** El fondo decorativo «z3» (nube con «zzz», reloj y gotas) es el mismo arte de la versión web (`web/assets/fondo-z3.svg`) convertido a `VectorDrawable`. El ícono de la aplicación es el emoji ⏰.

## Despliegue y visualización

### Requisitos

- JDK 17 o superior.
- Android SDK con la plataforma 36.1 (Gradle la descarga si las licencias están aceptadas).
- Android Studio reciente (opcional).
- Un emulador o un celular con Android 8.0 (API 26) o superior y depuración USB activada.
- Un archivo `mobile/local.properties` con `sdk.dir=/ruta/al/Android/Sdk` (Android Studio lo genera solo; también basta la variable de entorno `ANDROID_HOME`).

### Con Android Studio

Abrir la carpeta `mobile/` (*File > Open*), elegir el emulador o el celular y pulsar *Run*.

### Con la línea de comandos

```
cd mobile
./gradlew assembleDebug        # genera app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug         # compila e instala en el dispositivo o emulador conectado
```

Para instalar el APK en un celular sin Gradle: `adb install -r app/build/outputs/apk/debug/app-debug.apk`.

### Ejecutar en un emulador

```
# 1. Ver los emuladores y arrancar uno (agregar -no-window para no abrir ventana)
$ANDROID_HOME/emulator/emulator -list-avds
$ANDROID_HOME/emulator/emulator -avd Medium_Phone_API_36 &

# 2. Esperar a que Android termine de arrancar
adb wait-for-device
until [ "$(adb shell getprop sys.boot_completed | tr -d '\r')" = 1 ]; do sleep 2; done

# 3. Compilar e instalar
cd mobile && ./gradlew installDebug

# 4. Abrir la aplicación (o tocar el ícono del reloj en el lanzador)
adb shell am start -n com.agendalarm.app/.ui.alarmasdiavacio.M05AlarmasDiaVacioActivity

# 5. Al terminar
adb emu kill
```

### Cómo abrir cada pantalla

| Pantalla | Cómo llegar |
| --- | --- |
| M-05 · Alarmas del día · estado vacío | Es la pantalla de arranque: abrir la aplicación desde el lanzador |
| M-07 · Resumen de mañana | Tocar la mitad derecha de la pantalla en M-05 («día siguiente») |
| M-04 · Alarmas del día | Tocar «Ver alarmas» en M-07 |
| M-06 · Detalle de la alarma | Tocar una tarjeta de M-04 |
| M-09 · Método de confirmación | Tocar «Editar Alarma» en M-06 |
| M-11 · Levántate para apagar la alarma | Elegir «Ponerte de pie y sostener el teléfono» en M-09 |
| M-12 · Alarma cumplida | Pendiente de conectar desde M-11 |

### Ver las pantallas en el tamaño del diseño (390 × 844 dp)

Los mockups miden 390 × 844 dp. En el emulador se obtiene esa medida exacta con:

```
adb shell wm size 1170x2532
adb shell wm density 480
# al terminar, volver a la configuración original
adb shell wm size reset
adb shell wm density reset
```

### Pruebas y análisis estático

```
./gradlew testDebugUnitTest    # pruebas unitarias de ViewModels y estados (corren en la JVM, sin emulador)
./gradlew lintDebug            # análisis estático; el informe queda en app/build/reports/lint-results-debug.html
```
