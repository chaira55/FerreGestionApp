// Confirmación de eliminación
function confirmarEliminacion(mensaje = '¿Está seguro de eliminar este registro?') {
    return confirm(mensaje);
}

// Formatear números como moneda
function formatearMoneda(numero) {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: 'COP',
        minimumFractionDigits: 0
    }).format(numero);
}

// Mostrar alertas temporales
function mostrarAlerta(mensaje, tipo = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${tipo} alert-dismissible fade show`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);

    // Auto-cerrar después de 5 segundos
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// Validar formularios
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('.needs-validation');

    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});

// Busqueda en tiempo real en tablas
function filtrarTabla(inputId, tablaId) {
    const input = document.getElementById(inputId);
    const tabla = document.getElementById(tablaId);
    const tr = tabla.getElementsByTagName('tr');

    input.addEventListener('keyup', function() {
        const filtro = this.value.toUpperCase();

        for (let i = 1; i < tr.length; i++) {
            const td = tr[i].getElementsByTagName('td');
            let mostrar = false;

            for (let j = 0; j < td.length; j++) {
                if (td[j]) {
                    const txtValue = td[j].textContent || td[j].innerText;
                    if (txtValue.toUpperCase().indexOf(filtro) > -1) {
                        mostrar = true;
                        break;
                    }
                }
            }

            tr[i].style.display = mostrar ? '' : 'none';
        }
    });
}

// Calcular totales automáticamente
function calcularTotal() {
    let total = 0;
    const inputs = document.querySelectorAll('[data-subtotal]');

    inputs.forEach(input => {
        const valor = parseFloat(input.value) || 0;
        total += valor;
    });

    const totalElement = document.getElementById('total');
    if (totalElement) {
        totalElement.textContent = formatearMoneda(total);
    }
}

console.log('✅ FerreGestión JS cargado correctamente');