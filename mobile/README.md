# AgendaLarm — Mobile

App Android nativa (Kotlin + XML, Material 3). Es un proyecto Gradle completo: se abre en Android Studio con `File > Open` sobre `mobile/`.

Estado: base del proyecto lista (hoja de estilos, animaciones, tipografía y librerías) y **tres pantallas terminadas: M-04 · Alarmas del día, M-06 · Detalle de la alarma** (se abre tocando una tarjeta de M-04) **y M-09 · ¿Cómo quieres confirmar que despertaste?** (aún no la abre ninguna pantalla). Las demás se agregan encima, una por una. Sólo interfaz: no hay red, base de datos ni lógica de negocio; los datos son de muestra.

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
./gradlew testDebugUnitTest  # pruebas unitarias de los ViewModels (corren en la JVM, sin emulador)
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

# 4. Abrir M-04 (o tocar el ícono del reloj en el lanzador); M-06 se abre tocando una tarjeta
adb shell am start -n com.agendalarm.app/.ui.alarmasdia.M04AlarmasDelDiaActivity

# 5. Al terminar
adb emu kill
```

M-06 no se lanza directo con `adb` porque su Activity no está exportada: siempre se llega a ella desde M-04. M-09 sí se puede abrir directo en las compilaciones **debug** (su Activity se exporta sólo en `app/src/debug/AndroidManifest.xml`):

```
adb shell am start -n com.agendalarm.app/.ui.metodoconfirmacion.M09MetodoConfirmacionActivity
```

O desde Android Studio: `Run > Edit Configurations > Launch: Specified Activity`. En release no está exportada.

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
    │   ├── alarmasdia/                     # M-04: Activity, ViewModel, estado, adaptador, datos de muestra
    │   ├── detallealarma/                  # M-06: Activity, ViewModel, estado, datos de muestra
    │   └── metodoconfirmacion/             # M-09: Activity, ViewModel, estado, adaptador
    └── res/
        ├── layout/       activity_m04_alarmas_del_dia · activity_m06_detalle_alarma · activity_m09_metodo_confirmacion · include_encabezado_pantalla · include_dato_etiquetado · item_alarma · item_seccion · item_nota · item_introduccion · item_opcion_metodo
        ├── values/       colors_paleta · colors · dimens · styles_texto · styles_componentes · themes · animaciones · strings
        ├── color/        sel_*: colores por estado (reposo / pulsado / deshabilitado) de botones
        ├── font/         Inter Regular, Medium, SemiBold y Bold, más inter.xml (la familia completa)
        ├── drawable/     fondo_z3 · ilustracion_campana · ic_mas_opciones · punto_lugar · icono_metodo · interruptor_pista · interruptor_pulgar · campo_fondo
        ├── drawable-nodpi/ ic_launcher_foreground.png
        ├── mipmap-anydpi/  ic_launcher.xml (ícono adaptable)
        ├── anim/         transicion_entrar · transicion_salir · transicion_quedar
        ├── interpolator/ ease_in_out
        └── transition/   transicion_estandar
```

Fuera de `src/main/`: las pruebas unitarias de los ViewModels y estados viven en `app/src/test/java/com/agendalarm/app/ui/` (`detallealarma/`, `metodoconfirmacion/`) y `app/src/debug/AndroidManifest.xml` exporta M-09 sólo en debug.

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

**Color.** `colors_paleta.xml` guarda los 96 pasos de la lámina «06 · Paleta de colores» (primario, secundario, acento, neutros, error, confirmación, atención, informativo; 990 → 25, el 500 es la base). Las pantallas no los usan directamente: pasan por los tokens de `colors.xml` (`superficie`, `tarjeta`, `texto_primario`, `texto_secundario`, `borde_tarjeta`, `boton`, `exito`…), igual que web usa variables CSS. Los tokens móviles salen de esa paleta, por lo que algunos difieren algo de los muestreados a ojo en web (p. ej. botón `#8E4F19` = acento 900). Los pocos colores sin paso equivalente están muestreados del mockup y marcados como tales: pista y borde del interruptor apagado, y los dos tonos de la campana de M-06 (`campana_clara` `#F7D460`, `campana_oscura` `#E58E14`). `divisor` es el blanco de la línea que separa bloques en M-06.

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
| `EtiquetaDato` | 13 · Regular, texto secundario | etiqueta de un bloque de datos (M-06) |
| `CuerpoMarca` | 15 · Regular, color de marca | lugar del compromiso (M-06) |
| `OpcionTitulo` | 16 · SemiBold | título de un selector de opción (M-09) |
| `EstadoActivo` | 22 · Medium, verde `exito` | indicador «Activo» (M-09) |
| `Detalle` | 12 · Regular | detalle de tarjeta, nota al pie |
| `Micro` | 11 · Regular | acceso y código del encabezado |
| `Boton` | 14 · SemiBold | botones |
| `Reloj`, `ValorGrande`, `Valor` | 66 · SemiBold, 34 · Bold, 24 · SemiBold | cifras de M-10 y M-06 |

Cada `TextAppearance` trae su propio peso. **No** fijar `android:fontFamily` en el tema: a ese nivel pisa el de los `TextAppearance` y todo sale en Regular.

**Componentes** (`styles_componentes.xml`). Se aplican solos por el tema (`materialButtonStyle`, `materialCardViewStyle`, `switchStyle`, `editTextStyle`, `textViewStyle`).
- Verificados en pantalla contra el mockup: `Tarjeta` (borde 1 dp, radio 12 dp) e `Interruptor` (pista 44 × 24 dp, pulgar 18 dp) en M-04; `Boton.Primario` (48 dp, radio 10 dp, relleno acento 900, borde primario 990), `Boton.Destructivo` y `Campo` (52 dp, sangría 16 dp, placeholder 14 sp) comparados con los mockups del PDF en una pantalla de prueba ya retirada: tamaños, colores de relleno y posición del texto coinciden. `Boton.Primario` y `Boton.Destructivo` se verificaron de nuevo en M-06 (342 × 48 dp, radio 10 dp, relleno, borde y **estado pulsado**: `#211306` y `#8D2F2F`, igual que define el DS).
- Selector de opción (`item_opcion_metodo.xml`, M-09): tarjeta de 350 × 164 dp con **radio 13 dp** (1 dp más que `Tarjeta`, medido sobre el mockup), marcador cuadrado, título, interruptor y el indicador «Activo». El `Interruptor` se verificó de nuevo en M-09 (mismos colores de pista y pulgar, encendido y apagado).
- Bloque de datos (`include_dato_etiquetado.xml`): etiqueta `EtiquetaDato` sobre un valor `Valor`, con las variables `etiqueta` y `valor`; se lee como una sola parada en TalkBack. Se usa dos veces en M-06.
- Por verificar al construir su pantalla: `Boton.Secundario` y el borde del campo enfocado. El estado deshabilitado se vio en pantalla (fondo gris claro, texto atenuado).

**Ícono de la app.** El mismo emoji del reloj despertador que las 8 pantallas de web usan de favicon (`<text>⏰</text>`), en su versión Noto Color Emoji de Google (licencia en `assets/licencias/Noto-Emoji-OFL.txt`). Es un ícono adaptable: primer plano PNG de 432 × 432 px con el dibujo dentro de la zona segura de 66 dp, sobre el color `superficie`. Con `minSdk 26` no hacen falta PNG heredados. La pantalla de arranque de Android 12+ lo reutiliza.

**Interacción.** Los botones y campos responden al toque como en web —estado pulsado (un paso más oscuro), foco, deshabilitado, transición al mostrar cosas— pero **no validan ni guardan datos**: lo que cambia vive en memoria (ViewModel) y se pierde al cerrar la app. En M-04 lo interactivo son los interruptores y las tarjetas (abren M-06); en M-06 los dos botones muestran el estado pulsado pero no ejecutan nada. Ninguna de las dos pantallas tiene campos de texto.

**Movimiento.** Una sola curva y una sola duración para todo, como en web (Figma: smart animate, ease in and out, 220 ms):
- Entre pantallas: fundido cruzado definido en el tema; no requiere código al llamar `startActivity`. Comprobado en el emulador con las animaciones del sistema al ×10: apertura (la nueva pantalla se funde encima) y cierre (se desvanece dejando ver la anterior) sin oscurecerse en medio.
- Dentro de una pantalla: `contenedor.conTransicion { … }` anima mostrar/ocultar y agregar/quitar vistas.
- Listas: `recycler.usarAnimacionesAgendaLarm()`.
- Interruptor: la pista se funde en 150 ms (`@integer/duracion_interruptor`).

**Librerías** (`gradle/libs.versions.toml`): AndroidX Core, AppCompat, Material 3, ConstraintLayout, RecyclerView, Transition, Activity KTX, Lifecycle (ViewModel + SavedState + LiveData) y, para pruebas, JUnit, AndroidX Test y Espresso. Data binding y view binding activados. Si se agregan diálogos, menús o snackbars, mapear los `textAppearance*` del tema a Inter (ver `styles_texto.xml`).

## Pantallas

- `M04AlarmasDelDiaActivity` — Sebastián Vega
- `M06DetalleAlarmaActivity` — Sebastián Vega
- `M09MetodoConfirmacionActivity` — Sebastián Vega

### M-04 · Alarmas del día

Pantalla principal de consulta diaria: encabezado con fecha y número de compromisos, y una tarjeta por alarma con el compromiso que la origina y su interruptor.

- Reproduce el mockup completo: franja blanca de 70 dp con título, tipo de acceso («Requiere autenticación») y código «M4», tal como están dibujados en el PDF; tarjetas de 350 × 78 dp; interruptores; nota final; y el fondo decorativo `fondo_z3` (405 × 301 dp, detrás de la lista).
- `fondo_z3` es el mismo arte que `web/assets/fondo-z3.svg`, convertido a VectorDrawable. En los mockups móviles se ve ≈ 25 % más opaco que en web, así que la conversión multiplica todas sus opacidades por 1,25; la escala (405 × 301 dp, algo estirada en vertical) y la posición (2 dp del borde, 208 dp bajo la franja) se ajustaron por correlación contra M-04 y M-05.
- La franja se extiende bajo la barra de estado (`BaseActivity.aplicarInsets(encabezado, contenido)`); la lista se desplaza por debajo de la barra de navegación.
- Título, tarjetas y nota son **una sola lista** (`RecyclerView` con `ListAdapter` + DiffUtil y tres tipos de fila), así que se desplazan juntos.
- Interacción: cada interruptor cambia su alarma en el `AlarmasDelDiaViewModel` (LiveData); el estado sobrevive a rotaciones. Nada se guarda: al cerrar la app vuelve a los datos de muestra (`AlarmasMuestra.kt`).
- Tocar una tarjeta abre su detalle (M-06); tocar el interruptor sólo lo cambia. TalkBack anuncia la acción de la tarjeta como «ver detalle de la alarma». Igual que en web, sólo se enlaza lo que ya está construido: M-05 y el resto de pantallas todavía no.
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
- Por verificar en un dispositivo, en la primera pantalla que los use: foco del campo y `Boton.Secundario` (el estado pulsado de los botones primario y destructivo ya se comprobó en M-06).
- `android:dataExtractionRules` (aviso de lint heredado del andamio, junto con `allowBackup="false"`).

### M-06 · Detalle de la alarma

Información completa del compromiso que origina una alarma —nombre, hora, lugar, hora de salida estimada y margen calculado— con las acciones **Editar Alarma** y **Eliminar Alarma**. Se abre tocando una tarjeta de M-04.

- Reproduce el mockup completo: la franja «Detalle de la alarma · Requiere autenticación · M6», la campana, el título (22 sp), la hora grande (34 sp Bold), el lugar precedido de un punto, un divisor, dos bloques de datos (etiqueta 13 sp + valor 24 sp), dos botones de 342 × 48 dp y el fondo `fondo_z3`.
- **Campana** (`drawable/ilustracion_campana.xml`): no existe en web ni como archivo del proyecto, así que se reconstruyó sobre la figura del PDF con formas simples (círculos, arcos y curvas) y sus dos tonos son tokens. Medido sobre lo dibujado en el emulador, coincide con el mockup en 98,8 % (silueta), 97,1 % (amarillo) y 95,6 % (naranja). Los arcos de vibración, que en el PDF son un punteado casi invisible, van como cuatro trazos continuos al 6 % de opacidad. Está 8 dp a la izquierda del centro, como en el mockup (`detalle_ilustracion_sesgo`).
- Los tres puntos «⋯» junto al título son un adorno del PDF (`ic_mas_opciones.xml`), no un control: no hay ningún menú diseñado y no figuran entre los componentes de la pantalla.
- MVVM como en M-04: `DetalleAlarmaUi` (id, título, hora, lugar, hora de salida, margen en minutos) llega a la vista dentro de `DetalleAlarmaUiState` (`Alarma` o `NoEncontrada`), que expone `DetalleAlarmaViewModel`. El id de la alarma viaja en el Intent (`M06DetalleAlarmaActivity.intent(contexto, id)`) y el ViewModel lo lee de `SavedStateHandle`, así que sobrevive a rotaciones y a que el sistema cierre el proceso; si no corresponde a ninguna alarma, la pantalla se cierra.
- Datos de muestra (`DetalleAlarmaMuestra.kt`): uno por cada tarjeta de M-04, con su mismo id, título y hora. **Sólo «Clase Bases de Datos» trae los textos reales del mockup**; los otros tres se inventaron para poder abrir el detalle desde cualquier tarjeta.
- «Editar Alarma» y «Eliminar Alarma» muestran el estado pulsado pero no hacen nada: los mockups no incluyen una pantalla de edición ni una confirmación de borrado, y no hay fuente de datos que modificar.
- El contenido va en un `NestedScrollView`: se desplaza si no cabe (pantallas bajas, horizontal, texto grande). Cada bloque cuelga del anterior, así que un lugar largo o una fuente mayor empujan lo que sigue en vez de solaparlo.
- Atrás vuelve a M-04, que conserva el estado de sus interruptores. `AndroidManifest.xml` declara a M-04 como `parentActivityName`; la Activity no está exportada.
- Accesibilidad: el nombre de la alarma es un encabezado; cada bloque de datos se anuncia entero («Hora de salida estimada, 07:52»); la campana y los tres puntos son decorativos; la ventana anuncia su título; el texto respeta el tamaño de fuente del sistema.

**Verificación contra el mockup** (mismo método que M-04, con la figura de la página 9: `pdfimages -png -f 9`). La captura se alinea quitando la barra de estado de 24 dp y se comparan cajas de tinta, colores y forma:

| Elemento | Diferencia app / mockup |
| --- | --- |
| Campana, título, «⋯», hora, punto, lugar, etiquetas, valores y botones (posición y alto) | ≤ 0,5 dp |
| Ancho de los textos | +2 dp en el lugar (238 dp), de −1,5 a +0,5 dp en el resto: Inter 4.0 mide un poco distinto que la versión de Figma |
| Divisor | mismo alto y posición (376–377 dp); 0,5 dp más largo por la derecha |
| Punto del lugar | 11 dp de diámetro (mockup 11,4 dp), centro a ≤ 0,1 dp |
| Colores planos (fondo, franja, relleno de botones, campana) | idénticos |
| Colores de texto | son tokens del DS; frente al mockup difieren lo mismo que el «acceso» de M-04, que no cambió (el JPEG del PDF oscurece +3 a +5 de luma) |
| Radio y borde de los botones | perfil de la esquina idéntico (±1 px a 2×) |
| Estado pulsado | «Editar» `#211306`, «Eliminar» `#8D2F2F`; al soltar vuelven al reposo |
| Fondo decorativo | 1 dp a la izquierda y 8 dp más arriba que en M-04 (calculado por correlación contra M-05); alineado a ≤ 0,5 dp; intensidad global comparable (dentro de ±15 %) |

Comprobado además en el emulador: cada una de las 4 tarjetas abre su propio detalle; el interruptor no abre el detalle; Atrás y la rotación conservan el estado; en horizontal el contenido se centra a ≤ 420 dp y se desplaza hasta los botones; al 130 % de fuente el lugar pasa a dos líneas con el punto en la primera y nada se solapa; con las animaciones al ×10 la apertura y el cierre son un fundido cruzado sin oscurecerse; `lintDebug` termina con 0 errores (los mismos 3 avisos heredados) y `testDebugUnitTest` pasa sus 4 pruebas.

**Decisiones y avisos de diseño**
- «M6» y «Requiere autenticación» son anotaciones del PDF y se reprodujeron, igual que en M-04 (`m06_*` en `strings.xml`).
- La línea divisoria del mockup llega 16 dp más allá del texto por la derecha (de 32 a 374,5 dp, mientras los botones acaban en 366); se reprodujo (`detalle_divisor_margen_final`).
- El contraste del texto blanco sobre el botón destructivo (≈ 2,1 : 1) ya está anotado en M-04; se conserva como en el mockup.
- **Aviso, fondo `fondo_z3` (heredado de M-04):** en el PDF el trazo del fondo es más grueso (las «zzzz», el reloj y la nube se ven como trazos anchos), mientras que el arte de web convertido a VectorDrawable los tiene finos. A contraste normal apenas se nota; igualarlo exigiría regenerar `fondo_z3.xml` desde el PDF y afectaría también a M-04.

### M-09 · ¿Cómo quieres confirmar que despertaste?

Selección del método con el que se confirma el despertar: **Ponerte de pie y sostener el teléfono** o **Registro fotográfico**. Una tarjeta por método, cada una con su interruptor; la activa muestra «Activo».

- Reproduce el mockup completo: la franja con el título en dos líneas, «Requiere autenticación» y «M9»; el texto «Elige el método por tipo de alarma»; dos tarjetas de 350 × 164 dp con un marcador cuadrado de 35 dp, el título del método (16 sp SemiBold), el interruptor y, en la activa, «Activo» (22 sp Medium en verde `exito`); y el fondo `fondo_z3`.
- **Encabezado de dos líneas.** En este mockup el título va más arriba y 4 dp más a la izquierda, y el acceso se le pega. `include_encabezado_pantalla` tiene ahora la variable `tituloDoble` (`app:tituloDoble="@{true}"`); sin ella, M-04 y M-06 se dibujan exactamente igual (comprobado píxel a píxel antes y después). Como el título ya no cabe en la franja de 70 dp con el relleno de siempre, la variante usa un relleno inferior menor (`encabezado_relleno_inferior_doble`).
- **Dos variantes de tarjeta.** La activa lleva el contenido 8 dp más arriba y el marcador 2 dp a la izquierda que la que está en reposo (así están en el mockup: se midieron en las dos tarjetas). Esas distancias son relleno del título y del marcador (`opcion_*_activa` / `opcion_*_reposo` en `dimens.xml`) y se eligen con data binding (`opcion.activa ? … : …`), sin adaptadores propios. Al cambiar de método sólo se anima la fila que cambió (payload de DiffUtil + `conTransicion`): «Activo» se funde y el contenido se reacomoda.
- **Elección exclusiva.** Tocar una tarjeta o encender su interruptor la activa y apaga la otra. El interruptor de la tarjeta activa no se puede apagar (siempre hay un método): vuelve a quedar encendido. El PDF dice «entre ponerse de pie … o registrar una fotografía»; el comportamiento exacto es una decisión nuestra, fácil de cambiar en `MetodoConfirmacionUiState`.
- MVVM como M-04: `MetodoConfirmacionViewModel` expone un `MetodoConfirmacionUiState` (el método activo) y la vista sólo dibuja `elementos()` —introducción y una tarjeta por método— con `OpcionesAdapter` (`ListAdapter` + DiffUtil). La regla «un solo método activo» vive en el estado y se prueba en la JVM. Nada se guarda: al cerrar la app vuelve a «Ponerte de pie y sostener el teléfono», el que trae activo el mockup.
- Los dos métodos son valores fijos (`MetodoConfirmacion`) con sus textos en `strings.xml`; aquí no hay datos de muestra.
- El marcador cuadrado (`icono_metodo.xml`: 35 dp, borde de 5,75 dp en el color de marca, centro vacío) es un dibujo del PDF que ocupa el lugar de un icono por definir; es decorativo para TalkBack.
- El mockup no dice desde dónde se llega a esta pantalla y ninguna de las construidas la abre, así que no hay enlace: se abre directo en debug (ver «Correr la app en local»).
- Fondo: mismo arte y mismo desfase horizontal que en M-04 (2 dp), 8 dp más arriba (200 dp bajo la franja). El fondo del PDF de M-09 es exactamente el de M-05 desplazado 8 dp (mismo raster), así que la posición no es una estimación.
- Accesibilidad: cada interruptor se anuncia con el nombre del método; la acción de la tarjeta es «elegir este método»; el título del encabezado es un encabezado; el marcador y el fondo son decorativos; el texto respeta el tamaño de fuente del sistema (al 130 % el título del encabezado y el de la tarjeta pasan a más líneas sin solaparse).

**Verificación contra el mockup** (mismo método que M-04, con la figura de la página 12: `pdfimages -png -f 12`; captura alineada quitando la barra de estado de 24 dp; esperar ≥ 8 s tras abrir la pantalla, porque una captura a mitad del fundido de entrada sale desvaída):

| Elemento | Diferencia app / mockup |
| --- | --- |
| Título (2 líneas), acceso, «M9», texto introductorio (posición y alto) | ≤ 0,5 dp |
| Tarjetas (posición, ancho, alto, separación de 24 dp) | ≤ 0,5 dp |
| Marcadores, títulos de opción, «Activo» e interruptores (posición y tamaño) | ≤ 0,5 dp |
| Ancho de los textos | +2 dp en la primera línea del título (255 dp), −2,5 dp en «Ponerte de pie y sostener el», ≤ 1 dp en el resto: Inter 4.0 mide un poco distinto que la versión de Figma |
| Colores planos (fondo, franja, interior y borde de tarjeta, marcador, pista y pulgar de los interruptores) | idénticos |
| Colores de texto | son tokens del DS; frente al mockup difieren lo mismo que el «acceso» de M-04 (el JPEG del PDF oscurece +3 a +5 de luma) |
| Peso de «Activo» | Medium: la tinta coincide (cociente 1,00; con Regular era 0,84) |
| Radio de las tarjetas | 13 dp: perfil de la esquina a ≤ 1 px (a 2×) en todas las filas (con 12 o 14 dp había hasta 3 px) |
| Variante de cada tarjeta al cambiar de método | idéntica a la de la otra tarjeta del mockup (marcador, título, «Activo» e interruptor a ≤ 0,5 dp) |
| Fondo decorativo | alineado; ver el aviso de trazo más grueso en M-06 |

Comprobado además en el emulador: elegir la otra tarjeta mueve «Activo» y los interruptores; tocar el interruptor de la tarjeta activa no lo apaga; encender el de la otra la activa; rotar y volver conserva la elección; en horizontal el contenido se centra a ≤ 420 dp; con las animaciones al ×10 la elección se anima sin parpadeos; M-04 y M-06 quedan idénticos píxel a píxel tras tocar el encabezado compartido; `lintDebug` termina con 0 errores y `testDebugUnitTest` pasa sus 9 pruebas.

**Decisiones y avisos de diseño**
- «M9» y «Requiere autenticación» son anotaciones del PDF y se reprodujeron (`m09_*` en `strings.xml`), igual que en M-04 y M-06.
- Las dos tarjetas del mockup no están alineadas entre sí (el marcador de la de reposo queda 2 dp a la derecha y el contenido 8 dp más abajo). Se reprodujo tal cual porque el objetivo es la fidelidad; si el equipo prefiere una sola posición, basta igualar las parejas `opcion_*_activa` / `opcion_*_reposo` de `dimens.xml`.
- El radio de estas tarjetas (13 dp) no es el de `Tarjeta` (12 dp): es el que muestra el mockup.
- El aviso del fondo `fondo_z3` (trazo más fino que el del PDF) descrito en M-06 vale también aquí.
- Al agregar `src/debug/AndroidManifest.xml`, lint dejó de reportar el aviso heredado del ícono sin capa monocromática (el ícono no cambió: sigue pendiente).

## Convenciones

- Una pantalla = una Activity (extiende `BaseActivity`) + un layout, en su paquete bajo `ui/`. El nombre lleva el código del mockup: `M04AlarmasDelDiaActivity` ↔ `activity_m04_alarmas_del_dia.xml`; ViewModel, adaptador y estado usan el nombre sin código.
- Encabezado de cada pantalla: `<include layout="@layout/include_encabezado_pantalla" …>` con `titulo`, `acceso` y `codigo`; luego `aplicarInsets(encabezado, contenido)`. Con un título de dos líneas se agrega `app:tituloDoble="@{true}"` (M-09). Las pantallas sin franja usan `aplicarInsets(raiz)`; ambas suman los insets al relleno del layout.
- Botones y campos: interactivos como en web (pulsado, foco, transición) sin validar ni guardar; si una pantalla necesita conservar algo mientras está abierta, va en su ViewModel.
- Patrón MVVM: la vista sólo dibuja un estado (`…UiState`) que expone el ViewModel; los eventos suben con funciones `alternarAlarma(…)`.
- Sin colores, tamaños ni textos fijos en el layout: usar `@color/` (tokens), `@dimen/`, `@string/` y los estilos de la hoja. Si un valor nuevo sale de un mockup, se agrega a `dimens.xml` con un comentario.
- Layouts con ConstraintLayout y `layout_constraintWidth_max="@dimen/ancho_max_contenido"` para no romperse en pantallas grandes.
- `SwitchCompat` mide la altura de su texto vacío y centra la pista ahí: cualquier interruptor nuevo necesita el `minHeight` que ya trae `Widget.AgendaLarm.Interruptor`.
- Los comentarios XML no pueden contener dos guiones seguidos.
- Una pantalla que recibe un dato lo recibe en el Intent, con una fábrica en su Activity (`M06DetalleAlarmaActivity.intent(contexto, id)`), y su ViewModel lo lee de `SavedStateHandle`: sobrevive a rotaciones y a que el sistema cierre el proceso.
- Un `View` simple con `wrap_content` llena todo el ancho disponible (sólo los widgets con contenido propio miden lo que muestran): a puntos, líneas y demás vistas sin contenido hay que darles tamaño explícito.
- No repetir en un layout los ids que trae un `include` (el encabezado ya usa `texto_titulo`, `texto_acceso` y `texto_codigo`): lint avisa y las restricciones podrían resolverse contra la vista equivocada.
- La lógica de cada ViewModel se prueba en la JVM (`app/src/test/`, `./gradlew testDebugUnitTest`), sin emulador.
- Cuando un componente tiene variantes que sólo difieren en posición (tarjeta activa / en reposo, encabezado de dos líneas), las distancias van como relleno (`android:paddingTop="@{cond ? @dimen/a : @dimen/b}"`): data binding no trae adaptador para los márgenes y así no hace falta escribir uno propio. Definir también `paddingEnd` para que lint no avise de simetría RTL.
- Una pantalla que ninguna otra abre (o que pide un dato) se exporta sólo en `app/src/debug/AndroidManifest.xml`, para poder abrirla directo mientras se revisa contra su mockup; en release no se exporta.
- Capturas para comparar con el PDF: esperar ≥ 8 s tras abrir la pantalla; una captura a mitad de un fundido sale desvaída y se parece a una regresión.
- Declarar cada Activity nueva en `AndroidManifest.xml`. Un commit por pantalla.
