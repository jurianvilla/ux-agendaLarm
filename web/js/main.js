/* =============================================================================
   AgendaLarm · Comportamiento compartido de las pantallas
   -----------------------------------------------------------------------------
   Prototipo de front: no guarda ni procesa datos. Sólo resuelve la navegación
   entre pantallas, el cambio de vista dentro de una misma pantalla y la
   apertura de ventanas modales (como el selector de origen de W-09).

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
     [data-elegir-origen]        → opción del selector de calendario (W-09)
============================================================================= */

(() => {
  'use strict';

  const CLASE_OCULTO = 'esta-oculto';

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

  document.addEventListener('DOMContentLoaded', () => {
    activarNavegacion();
    activarVistas();
    activarModales();
    activarSelectorCalendario();
  });
})();
