/* =============================================================================
   AgendaLarm · Comportamiento compartido de las pantallas
   -----------------------------------------------------------------------------
   Prototipo de front: no guarda ni procesa datos. Sólo resuelve la navegación
   entre pantallas, el cambio de vista dentro de una misma pantalla, la
   apertura de ventanas modales y las pequeñas simulaciones (carga de foto,
   cámara, selector de origen) que hacen falta para que el flujo se sienta real.

   Cada cambio visual pasa por conTransicion() para reutilizar exactamente la
   misma transición ya declarada en css/styles.css (@view-transition / las
   reglas ::view-transition-old(root) y ::view-transition-new(root)): la
   navegación entre archivos la dispara el navegador solo, y aquí forzamos la
   misma animación para los cambios dentro de una misma página.

   Contratos de marcado (atributos data-*):
     [data-nav="archivo.html"]   → navega a otra pantalla
     [data-vista]                → sección que representa una vista (usa su id)
     [data-mostrar-vista="id"]   → muestra esa vista y oculta las demás
     [data-abrir="#id"]          → abre el <dialog> indicado
     [data-cerrar]               → cierra el <dialog> que lo contiene
     [data-quitar-fila]          → descarta el <li> que lo contiene
     [data-editar]               → alterna edición en línea del texto del <li>
     [data-elegir-origen]        → opción del selector de calendario (W-09)
============================================================================= */

(() => {
  'use strict';

  const CLASE_OCULTO = 'esta-oculto';
  const DESTINO_VERIFICACION_FOTO = 'W-11-verificacion-de-foto.html';
  const ESPERA_CARGA_MS = 3000;

  /* --- Transición compartida ---------------------------------------------- */

  function conTransicion(cambio) {
    if (typeof document.startViewTransition !== 'function') {
      cambio();
      return;
    }
    const transicion = document.startViewTransition(cambio);
    // Si el navegador salta o interrumpe la animación (clics muy seguidos,
    // pestaña no visible) sus promesas se rechazan; el cambio ya se aplicó,
    // así que sólo evitamos el "Uncaught (in promise)" en consola.
    transicion.ready.catch(() => {});
    transicion.finished.catch(() => {});
  }

  /* --- Navegación entre pantallas ---------------------------------------- */

  function activarNavegacion() {
    document.querySelectorAll('[data-nav]').forEach((elemento) => {
      elemento.addEventListener('click', () => {
        window.location.href = elemento.dataset.nav;
      });
    });
  }

  /* --- Vistas dentro de una misma pantalla -------------------------------- */

  function mostrarVista(id) {
    const vistas = document.querySelectorAll('[data-vista]');
    const destino = document.getElementById(id);
    if (!destino || !vistas.length) return;

    vistas.forEach((vista) => vista.classList.toggle(CLASE_OCULTO, vista !== destino));
    // El hash permite enlazar directamente a una vista desde otra pantalla.
    history.replaceState(null, '', `#${id}`);
  }

  function activarVistas() {
    document.querySelectorAll('[data-mostrar-vista]').forEach((elemento) => {
      elemento.addEventListener('click', () => {
        conTransicion(() => mostrarVista(elemento.dataset.mostrarVista));
      });
    });

    const idInicial = window.location.hash.slice(1);
    if (idInicial) mostrarVista(idInicial);
  }

  /* --- Filas descartables (Verificación de foto) --------------------------- */

  function activarFilasDescartables() {
    document.querySelectorAll('[data-quitar-fila]').forEach((boton) => {
      boton.addEventListener('click', () => {
        conTransicion(() => boton.closest('li')?.classList.add('esta-descartada'));
      });
    });
  }

  /* --- Edición en línea del texto de una fila (Verificación de foto) ------- */

  function activarEdicionInline() {
    // Nota: a diferencia del resto de interacciones, activar la edición NO
    // pasa por conTransicion(). Necesita enfocar el texto en el mismo evento
    // de clic (si el foco llega un instante después, algunos navegadores lo
    // ignoran) y no es un cambio de pantalla: es sólo activar un campo.
    document.querySelectorAll('[data-editar]').forEach((boton) => {
      boton.addEventListener('click', () => {
        const texto = boton.closest('li')?.querySelector('.lista-editable__texto');
        if (!texto) return;

        const entrando = !texto.isContentEditable;
        texto.contentEditable = String(entrando);
        texto.classList.toggle('lista-editable__texto--activo', entrando);

        if (entrando) {
          texto.focus();
          const rango = document.createRange();
          rango.selectNodeContents(texto);
          const seleccion = window.getSelection();
          seleccion?.removeAllRanges();
          seleccion?.addRange(rango);
        }
      });
    });

    function salirDeEdicion(texto) {
      if (!texto.isContentEditable) return;
      texto.contentEditable = 'false';
      texto.classList.remove('lista-editable__texto--activo');
    }

    // Enter/Escape confirman visualmente el cambio (nada se guarda igual).
    document.addEventListener('keydown', (evento) => {
      const objetivo = evento.target;
      if (!(objetivo instanceof HTMLElement)) return;
      if (!objetivo.classList.contains('lista-editable__texto')) return;
      if (evento.key === 'Enter' || evento.key === 'Escape') {
        evento.preventDefault();
        objetivo.blur();
      }
    });

    document.addEventListener(
      'focusout',
      (evento) => {
        const objetivo = evento.target;
        if (objetivo instanceof HTMLElement && objetivo.classList.contains('lista-editable__texto')) {
          salirDeEdicion(objetivo);
        }
      },
      true
    );
  }

  /* --- Ventanas modales --------------------------------------------------- */

  function activarModales() {
    document.querySelectorAll('[data-abrir]').forEach((disparador) => {
      disparador.addEventListener('click', () => {
        const modal = document.querySelector(disparador.dataset.abrir);
        if (modal instanceof HTMLDialogElement) conTransicion(() => modal.showModal());
      });
    });

    document.querySelectorAll('[data-cerrar]').forEach((boton) => {
      boton.addEventListener('click', () => {
        const modal = boton.closest('dialog');
        if (modal) conTransicion(() => modal.close());
      });
    });

    // Clic sobre el velo (fuera del recuadro) también cierra el modal.
    document.querySelectorAll('dialog').forEach((modal) => {
      modal.addEventListener('click', (evento) => {
        const caja = modal.getBoundingClientRect();
        const dentro =
          evento.clientX >= caja.left &&
          evento.clientX <= caja.right &&
          evento.clientY >= caja.top &&
          evento.clientY <= caja.bottom;
        if (!dentro) conTransicion(() => modal.close());
      });
    });
  }

  /* --- Selector de origen del calendario (W-09) ---------------------------- */

  function activarSelectorCalendario() {
    const bloque = document.getElementById('bloque-importado');
    document.querySelectorAll('[data-elegir-origen]').forEach((boton) => {
      boton.addEventListener('click', () => {
        const modal = boton.closest('dialog');
        conTransicion(() => {
          modal?.close();
          bloque?.classList.remove(CLASE_OCULTO);
        });
      });
    });
  }

  /* --- Carga simulada tras subir/tomar la foto (W-10) ----------------------- */

  function simularCargaYNavegar(antesDeCargar) {
    // antesDeCargar (opcional) entra en la MISMA transición que muestra la
    // vista de carga -por ejemplo, cerrar el modal de cámara- para no
    // encadenar dos animaciones que compitan entre sí.
    conTransicion(() => {
      antesDeCargar?.();
      mostrarVista('vista-cargando');
    });
    window.setTimeout(() => {
      window.location.href = DESTINO_VERIFICACION_FOTO;
    }, ESPERA_CARGA_MS);
  }

  function activarSubirFoto() {
    const boton = document.getElementById('boton-subir');
    const entrada = document.getElementById('entrada-archivo');
    const mensajeInvalido = document.getElementById('mensaje-tipo-invalido');
    if (!boton || !entrada) return;

    boton.addEventListener('click', () => entrada.click());

    entrada.addEventListener('change', () => {
      const archivo = entrada.files?.[0];
      entrada.value = ''; // El prototipo no guarda ni procesa el archivo elegido.
      if (!archivo) return;

      const esImagenValida = archivo.type === 'image/png' || archivo.type === 'image/jpeg';
      mensajeInvalido?.classList.toggle(CLASE_OCULTO, esImagenValida);
      if (!esImagenValida) return;

      simularCargaYNavegar();
    });
  }

  /* --- Simulación de cámara (W-10) ------------------------------------------ */

  function activarTomarFoto() {
    const boton = document.getElementById('boton-tomar');
    const modal = document.getElementById('camara-modal');
    const video = document.getElementById('camara-video');
    const marcoAlterno = document.getElementById('camara-marco');
    const botonCapturar = document.getElementById('camara-capturar');
    if (!boton || !modal || !botonCapturar) return;

    let flujo = null;

    function detenerCamara() {
      flujo?.getTracks().forEach((pista) => pista.stop());
      flujo = null;
      if (video) video.srcObject = null;
    }

    async function abrirCamara() {
      conTransicion(() => modal.showModal());

      const tieneCamara = typeof navigator.mediaDevices?.getUserMedia === 'function';
      try {
        if (!tieneCamara) throw new Error('getUserMedia no disponible');
        flujo = await navigator.mediaDevices.getUserMedia({ video: true });
        if (video) {
          video.srcObject = flujo;
          video.classList.remove(CLASE_OCULTO);
        }
        marcoAlterno?.classList.add(CLASE_OCULTO);
      } catch {
        // Sin cámara disponible, sin permiso, o abierto desde file:// (contexto
        // no seguro): se simula la captura igual con el marco alterno.
        video?.classList.add(CLASE_OCULTO);
        marcoAlterno?.classList.remove(CLASE_OCULTO);
      }
    }

    boton.addEventListener('click', abrirCamara);

    botonCapturar.addEventListener('click', () => {
      detenerCamara();
      simularCargaYNavegar(() => modal.close());
    });

    modal.addEventListener('close', detenerCamara);
  }

  document.addEventListener('DOMContentLoaded', () => {
    activarNavegacion();
    activarVistas();
    activarFilasDescartables();
    activarEdicionInline();
    activarModales();
    activarSelectorCalendario();
    activarSubirFoto();
    activarTomarFoto();
  });
})();
