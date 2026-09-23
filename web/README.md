# AgendaLarm — Web

Prototipo web de **AgendaLarm · Servicio de gestión de alarmas**: las pantallas con las que una persona configura su periodo académico o laboral (carga de horario, direcciones, margen de preparación y excepciones). Está hecho en HTML5, CSS3 y JavaScript, sin compilación ni dependencias.

## Datos del proyecto

| | |
| --- | --- |
| **Proyecto** | AgendaLarm · Servicio de gestión de alarmas |
| **Universidad** | Universidad de los Andes |
| **Programa** | MISO · Maestría en Ingeniería de Software |
| **Curso** | Diseño de Experiencia de Usuario para el Desarrollo de Software |
| **Docente** | Sergio Acosta |
| **Integrantes** | Juan Sebastián Vega Guarín · Julio César Urian Villamil |
| **Entregable** | Maquetación en código de la aplicación web (prototipo navegable) |
| **Herramienta de diseño** | Figma |
| **Editor de código** | Visual Studio Code |
| **Ciudad y fecha** | Bogotá D.C., septiembre de 2026 |

## Pantallas

| Código | Pantalla | Archivo | Responsable |
| --- | --- | --- | --- |
| W-05 | Panel principal | `W-05-panel-principal.html` | Julio César Urian Villamil |
| W-07 | Cargar tu horario | `W-07-cargar-horario.html` | Julio César Urian Villamil |
| W-08 | Registro manual | `W-08-registro-manual.html` | Julio César Urian Villamil |
| W-09 | Importar del calendario | `W-09-importar-del-calendario.html` | Juan Sebastián Vega Guarín |
| W-10 | Foto del horario | `W-10-foto-del-horario.html` | Juan Sebastián Vega Guarín |
| W-11 | Verificación de foto | `W-11-verificacion-de-foto.html` | Juan Sebastián Vega Guarín |
| W-12 | Direcciones y margen | `W-12-direcciones-margen.html` | Julio César Urian Villamil |
| W-13 | Excepciones del periodo | `W-13-excepciones-periodo.html` | Julio César Urian Villamil |

### Flujo de navegación

```
W-05 Panel principal
 └─ Configuración del periodo ─▶ W-07 Cargar tu horario
        ├─ Registro manual ─────────▶ W-08 ─▶ W-12 Direcciones y margen ─▶ W-13 Excepciones del periodo
        ├─ Importar desde calendario ▶ W-09 ─────────────────────────────▶ W-13
        └─ Foto del horario ────────▶ W-10 ─▶ W-11 Verificación de foto ─▶ W-13
```

### Descripción de cada pantalla

**W-05 · Panel principal.** Punto de partida de la aplicación. Muestra las cuatro secciones como tarjetas: *Configuración del periodo* (lleva a W-07), *Alarmas del periodo*, *Seguimiento* y *Cuenta*.

**W-07 · Cargar tu horario.** Primer paso de la configuración del periodo (indicador de tres pasos: Cargar horario, Direcciones y margen, Revisar propuesta). Ofrece tres formas de cargar el horario: *Registro manual* (lleva a W-08), *Importar desde calendario* (W-09) y *Foto del horario* (W-10).

**W-08 · Registro manual.** Alta de compromisos uno por uno para quien prefiere capturar el horario a mano. Tiene los campos *Día*, *Hora* y *Actividad*; **+ Agregar compromiso** añade una fila «Día · Hora · Actividad» al final de la lista (cuando los tres campos tienen contenido) y devuelve el foco al campo *Actividad*. Cada fila trae un botón para eliminarla. **Guardar y continuar** lleva a W-12.

**W-09 · Importar del calendario.** Conexión con el calendario académico o laboral. **Conectar calendario** abre una ventana emergente (`<dialog>`) para elegir el origen: Gmail, Hotmail o Uniandes. Al elegirlo aparece el bloque *Eventos importados*, con una casilla por evento para aceptarlo o desmarcarlo, y el botón **Procesar calendario**, que lleva a W-13.

**W-10 · Foto del horario.** Carga de una imagen del horario impreso, con dos orígenes: **Subir foto** abre el selector de archivos del sistema, restringido a PNG y JPG (con otro tipo de archivo muestra el mensaje «Solo se admiten imágenes PNG o JPG.»), y **Tomar foto** abre una ventana emergente que usa la cámara del dispositivo (`getUserMedia`) y, si no hay cámara o permiso, muestra un recuadro alterno. Con **Capturar** o con un archivo válido aparece la vista «Cargando tu horario…» durante 3 segundos y la aplicación pasa a W-11.

**W-11 · Verificación de foto.** Revisión de lo que el sistema interpretó de la imagen (por ejemplo «Lun · 08:00 · Cálculo I»). El lápiz de cada fila activa la edición en línea del texto (Enter, Escape o quitar el foco la confirman) y la `X` descarta la fila. **Todo correcto, continuar** lleva a W-13.

**W-12 · Direcciones y margen.** Segundo paso de la configuración del periodo. Captura la *dirección de origen*, la *dirección de destino* y el *margen de preparación* en minutos mediante un deslizador de 0 a 60 (de 5 en 5). **Guardar y continuar** lleva a W-13.

**W-13 · Excepciones del periodo.** Última pantalla del flujo. Muestra las *excepciones automáticas* (festivos precargados, cada uno con su interruptor para desactivarlo), la lista de *Tus excepciones* y el botón **+ Agregar excepción**.

En todas las pantallas los datos son de muestra: no se envía, guarda ni procesa información, y al recargar la página todo vuelve a sus valores iniciales.

## Tecnologías y versiones

| Tecnología | Versión | Uso |
| --- | --- | --- |
| HTML | HTML5 | Estructura de las pantallas (`<dialog>`, `<main>`, formularios) |
| CSS | CSS3 | Variables CSS, cuadrículas y flexbox, transición entre pantallas (`@view-transition`) |
| JavaScript | ECMAScript 2020 | Navegación, ventanas emergentes y simulaciones; sin módulos ni bibliotecas |
| Navegador recomendado | Chrome o Edge 126 o superior | Necesario para la animación entre pantallas; sin ella todo funciona igual |

No usa gestor de paquetes, compilador ni servidor: son archivos estáticos.

## Estructura de carpetas

```
web/
├── README.md
├── W-05-panel-principal.html            # W-05 · Panel principal
├── W-07-cargar-horario.html             # W-07 · Cargar tu horario
├── W-08-registro-manual.html            # W-08 · Registro manual
├── W-09-importar-del-calendario.html    # W-09 · Importar del calendario
├── W-10-foto-del-horario.html           # W-10 · Foto del horario
├── W-11-verificacion-de-foto.html       # W-11 · Verificación de foto
├── W-12-direcciones-margen.html         # W-12 · Direcciones y margen
├── W-13-excepciones-periodo.html        # W-13 · Excepciones del periodo
├── css/
│   └── styles.css                       # variables de color y espaciado, componentes y transición
├── js/
│   └── main.js                          # comportamiento compartido de todas las pantallas
└── assets/
    ├── fondo-z3.svg                     # fondo decorativo
    └── horario-foto.png                 # imagen de ejemplo del horario
```

Los archivos siguen el patrón `W-##-nombre.html` (número de dos cifras y guion, en mayúscula) y se enlazan entre sí por ese nombre.

## Sistema de diseño y comportamiento compartido

- **`css/styles.css`.** Las variables de `:root` (`--color-superficie`, `--color-tarjeta`, `--color-texto-primario`, `--color-boton`, `--radio-base`, `--espaciado-base`, `--ancho-contenido`…) definen la identidad visual. Los componentes usan nombres tipo BEM (`.tarjeta-seccion`, `.boton--secundario`, `.lista-editable__boton`). La transición de 220 ms *ease-in-out* entre pantallas se declara con `@view-transition`.
- **`js/main.js`.** Cada comportamiento se activa por atributos `data-*` en el HTML:

| Atributo | Efecto |
| --- | --- |
| `data-nav="archivo.html"` | Navega a otra pantalla |
| `data-vista` · `data-mostrar-vista="id"` | Muestra una vista dentro de la misma pantalla y oculta las demás |
| `data-foco-vista` | Recibe el foco al mostrarse su vista, para que el lector de pantalla anuncie el cambio (W-09, W-10) |
| `data-abrir="#id"` · `data-cerrar` | Abre y cierra un `<dialog>` |
| `data-quitar-fila` | Descarta la fila (`<li>`) que lo contiene |
| `data-editar` | Alterna la edición en línea del texto de una fila |
| `data-elegir-origen` | Opción del selector de calendario (W-09) |
| `data-agregar-compromiso="#lista"` | Agrega el compromiso escrito a la lista (W-08) |

## Despliegue y visualización

### Requisitos

- Google Chrome o Microsoft Edge 126 o superior (con otros navegadores funciona, pero sin la animación entre pantallas).
- Python 3 para servir la carpeta en local (opción 2). Sirve cualquier otro servidor de archivos estáticos.

### Cómo abrirlo

1. **Abrir directamente.** Abrir `W-05-panel-principal.html` en el navegador (doble clic o *Archivo > Abrir archivo*) y navegar desde ahí.
2. **Servir en local** (necesario para que la cámara de W-10 funcione, porque exige `localhost` o HTTPS):

```
cd web
python3 -m http.server 8000     # en Windows: py -m http.server 8000
```

y abrir `http://localhost:8000/W-05-panel-principal.html`. Con cualquier otro servidor de archivos estáticos el resultado es el mismo.
