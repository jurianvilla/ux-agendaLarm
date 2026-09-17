# AgendaLarm — Web

Maquetación plana en HTML/CSS/JS (sin build ni dependencias).

## Estructura

```
web/
├── w05-panel-principal.html   # W-05 · Panel principal
├── w07-cargar-horario.html   # W-07 · Cargar tu horario
├── W-09-importar-del-calendario.html   # W-09 · Importar del calendario
├── W-10-foto-del-horario.html   # W-10 · Foto del horario
├── W-11-verificacion-de-foto.html   # W-11 · Verificación de foto
├── css/styles.css   # tokens de color/espaciado + estilos base compartidos
├── js/main.js
└── assets/           # imágenes/íconos
```

## Pantallas

- `w05-panel-principal.html` — Julio Cesar Urian Villamil
- `w07-cargar-horario.html` — Julio Cesar Urian Villamil
- `W-09-importar-del-calendario.html` — Sebastián Vega
- `W-10-foto-del-horario.html` — Sebastián Vega
- `W-11-verificacion-de-foto.html` — Sebastián Vega

### W-05 · Panel principal

Accesos a las cuatro secciones de la app: Configuración del periodo (lleva a
W-07), Alarmas del periodo, Seguimiento y Cuenta.

### W-07 · Cargar tu horario

Paso 1 de configuración del periodo. Ofrece tres formas de cargar
el horario — Registro manual (lleva a W-08), Importar desde calendario y Foto
del horario —.

### W-09 · Importar del calendario

Conexión con el calendario académico o laboral (Gmail, Hotmail o Uniandes) y
revisión de los eventos importados antes de aceptarlos. Incluye, en el mismo
archivo, el estado de éxito posterior (equivalente a W-15 · Periodo
sincronizado): son dos vistas de una misma pantalla que se alternan con
JavaScript, sin recargar la página.

Al cargar, el bloque de eventos importados y el botón **Procesar calendario**
están ocultos. Interacciones del prototipo (sólo front, no se guardan ni
procesan datos):

- **Conectar calendario** abre un popup (`<dialog>`) para elegir el origen:
  Gmail, Hotmail o Uniandes, con su ícono centrado y su nombre.
- Elegir cualquier origen cierra el popup y revela el bloque de eventos
  importados junto con **Procesar calendario**, con la misma transición que
  usa el resto de la app para cambiar de pantalla (`@view-transition` del CSS,
  reutilizada desde `js/main.js` con `document.startViewTransition()`).
- El icono de verificación de cada evento marca y desmarca la fila (casilla
  real, accesible por teclado).
- **Procesar calendario** muestra la vista de éxito ("¡Tu periodo se
  sincronizó con tu celular!").
- Se puede abrir la vista de éxito directamente con su ancla:
  `W-09-importar-del-calendario.html#periodo-sincronizado`.

### W-10 · Foto del horario

Carga de una imagen del horario impreso, con dos alternativas de origen:
subir un archivo o tomar la fotografía en el momento. Interacciones del
prototipo (sólo front, no se guarda ni procesa ningún archivo):

- **Subir foto** abre el selector nativo del sistema operativo, restringido a
  PNG/JPG (`accept="image/png, image/jpeg"`). Si el archivo elegido no es de
  ese tipo se muestra un aviso y no pasa nada más; si es válido, el archivo
  se descarta igual (nunca se guarda ni se sube) y arranca la carga simulada.
- **Tomar foto** abre un popup (`<dialog>`) que intenta usar la cámara real
  del dispositivo (`getUserMedia`); si no hay cámara, no se concede el
  permiso o la página se abre desde `file://` (contexto no seguro), cae a un
  recuadro alterno con el mismo ícono de la pantalla, para que la interacción
  se sienta igual. **Capturar** cierra el popup y arranca la carga simulada.
- La carga simulada muestra un spinner ("Cargando tu horario…") durante 3
  segundos y navega automáticamente a `W-11-verificacion-de-foto.html`.
- Reutiliza la misma transición del resto de la app (`@view-transition`) para
  el cambio interno a la vista de carga y para la navegación final.

### W-11 · Verificación de foto

Revisión de lo que el sistema interpretó de la imagen. Cada evento se puede
corregir o descartar antes de continuar (sólo front, nada se persiste: al
refrescar la página todo vuelve a los valores iniciales).

- El lápiz de cada fila activa edición en línea (`contenteditable`) sobre el
  texto del evento; Enter, Escape o quitar el foco confirman visualmente el
  cambio, sin guardarlo en ningún lado.
- La `X` descarta la fila (se oculta con `display: none`); no hay forma de
  recuperarla salvo refrescando la página.
- **Todo correcto, continuar** navega a la vista de éxito de
  `W-09-importar-del-calendario.html` (W-15 · Periodo sincronizado).
- No tiene botón "Atrás": es el último paso de la verificación antes de
  confirmar.

