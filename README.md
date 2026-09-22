# AgendaLarm

**Servicio de gestión de alarmas para quienes estudian y trabajan al mismo tiempo.**

Prototipos de interfaz web y móvil de AgendaLarm, desarrollados para el curso *Diseño de Experiencia de Usuario para el Desarrollo de Software* de la Maestría en Ingeniería de Software.

## Datos del proyecto

| | |
| --- | --- |
| **Proyecto** | AgendaLarm · Servicio de gestión de alarmas |
| **Universidad** | Universidad de los Andes |
| **Programa** | MISO · Maestría en Ingeniería de Software |
| **Curso** | Diseño de Experiencia de Usuario para el Desarrollo de Software |
| **Docente** | Sergio Acosta |
| **Integrantes** | Juan Sebastián Vega Guarín · Julio César Urian Villamil |
| **Entregable** | Mockups de aplicación móvil y web, y componentes del Design System |
| **Herramienta de diseño** | Figma |
| **Ciudad y fecha** | Bogotá D.C., septiembre de 2026 |

## Descripción

AgendaLarm convierte el horario de una persona en alarmas con sentido. Cada alarma nace de un compromiso concreto (una clase, una reunión, una tutoría): la aplicación muestra qué compromiso la origina y dónde es, calcula la hora estimada de salida con su margen de preparación y, a la hora programada, pide confirmar que la persona despertó levantándose o registrando una fotografía.

El repositorio reúne dos prototipos que comparten identidad visual (paleta, tipografía Inter, componentes y movimiento):

- **Web:** configuración del periodo académico o laboral. La persona carga su horario (a mano, desde un calendario o con una foto), indica sus direcciones y su margen de preparación, y define las excepciones del periodo.
- **Móvil:** uso diario de las alarmas. Consulta de las alarmas del día (con o sin compromisos), resumen del día siguiente, detalle de cada alarma, elección del método de confirmación del despertar y confirmación de pie con barra de progreso.

## Plataformas

| Plataforma | Carpeta | Tecnología | Pantallas | Documentación |
| --- | --- | --- | --- | --- |
| Web | [`web/`](web/) | HTML5, CSS3 y JavaScript (ECMAScript 2020), sin dependencias | 8 (W-05, W-07 a W-13) | [`web/README.md`](web/README.md) |
| Móvil | [`mobile/`](mobile/) | Android nativo: Kotlin 2.2.10 + XML, Material 3 | 6 (M-04, M-05, M-06, M-07, M-09, M-11) | [`mobile/README.md`](mobile/README.md) |

## Estructura del repositorio

```
ux-agendaLarm/
├── README.md                    # este archivo
├── .gitignore
├── web/                         # prototipo web (páginas HTML + hoja de estilos + script)
│   ├── README.md
│   ├── W-05-panel-principal.html … W-13-excepciones-periodo.html
│   ├── css/styles.css
│   ├── js/main.js
│   └── assets/
└── mobile/                      # prototipo móvil (proyecto Gradle de Android Studio)
    ├── README.md
    ├── build.gradle.kts · settings.gradle.kts · gradle.properties
    ├── gradle/                  # wrapper de Gradle y catálogo de versiones
    └── app/                     # módulo de la aplicación (código Kotlin, layouts y recursos)
```

## Versiones de lenguajes y herramientas

| Componente | Versión |
| --- | --- |
| HTML | HTML5 |
| CSS | CSS3 (variables CSS y transiciones de vista) |
| JavaScript | ECMAScript 2020 |
| Kotlin | 2.2.10 |
| Java (JDK y nivel de lenguaje) | JDK 17 · nivel de lenguaje 11 |
| XML | Layouts, recursos y data binding de Android |
| Gradle · Android Gradle Plugin | 9.3.1 · 9.1.0 |
| Android SDK | compileSdk 36.1 · targetSdk 36 · minSdk 26 (Android 8.0) |
| Material Components | 1.13.0 (Material 3) |
| Tipografía | Inter 4.000 (licencia SIL OFL) |

## Cómo visualizar los prototipos

**Web.** No necesita instalación ni compilación. Abrir `web/W-05-panel-principal.html` en un navegador (se recomienda Google Chrome o Microsoft Edge 126 o superior) y navegar desde ahí. Para que la cámara de la pantalla «Foto del horario» funcione, servir la carpeta en local:

```
cd web
python3 -m http.server 8000     # abrir http://localhost:8000/W-05-panel-principal.html
```

**Móvil.** Requiere JDK 17 y el Android SDK. Con un emulador o un celular conectado con depuración USB:

```
cd mobile
./gradlew installDebug          # compila e instala la aplicación
```

También se puede abrir la carpeta `mobile/` con Android Studio (*File > Open*) y pulsar *Run*. El detalle de la instalación, las pruebas y la forma de abrir cada pantalla está en [`mobile/README.md`](mobile/README.md).
