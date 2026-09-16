# AgendaLarm — Web

Maquetación plana en HTML/CSS/JS (sin build ni dependencias).

## Estructura

```
web/
├── w05-panel-principal.html   # W-05 · Panel principal
├── w07-cargar-horario.html   # W-07 · Cargar tu horario
├── W-09-importar-del-calendario.html   # W-09 · Importar del calendario
├── css/styles.css   # tokens de color/espaciado + estilos base compartidos
├── js/main.js
└── assets/           # imágenes/íconos
```

## Pantallas

- `w05-panel-principal.html` — Julio Cesar Urian Villamil
- `w07-cargar-horario.html` — Julio Cesar Urian Villamil
- `W-09-importar-del-calendario.html` — Sebastián Vega

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


