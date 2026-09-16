// Navegación compartida entre pantallas. Cada pantalla enlaza este archivo.
// TODO: reemplazar por la lógica real de cada pantalla cuando se maquete el contenido.

document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('[data-nav]').forEach((el) => {
    el.addEventListener('click', () => {
      window.location.href = el.dataset.nav;
    });
  });
});
