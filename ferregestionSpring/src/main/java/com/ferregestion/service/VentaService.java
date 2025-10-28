package com.ferregestion.service;

import com.ferregestion.entity.Venta;
import com.ferregestion.entity.Cliente;
import com.ferregestion.entity.Cotizacion;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.VentaRepository;
import com.ferregestion.repository.ClienteRepository;
import com.ferregestion.repository.CotizacionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final CotizacionRepository cotizacionRepository;  // NUEVO

    public VentaService(VentaRepository ventaRepository,
                        ClienteRepository clienteRepository,
                        CotizacionRepository cotizacionRepository) {  // NUEVO
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.cotizacionRepository = cotizacionRepository;
    }

    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta con ID " + idVenta + " no encontrada"));
    }

    public Venta guardar(Venta venta) {
        // Validar cliente existente
        Cliente cliente = clienteRepository.findById(venta.getCliente().getCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con cédula " + venta.getCliente().getCedula() + " no encontrado"));
        venta.setCliente(cliente);

        // NUEVO: Validar cotización si viene en la petición
        if (venta.getCotizacion() != null && venta.getCotizacion().getIdCotizacion() != null) {
            Cotizacion cotizacion = cotizacionRepository.findById(venta.getCotizacion().getIdCotizacion())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cotización con ID " + venta.getCotizacion().getIdCotizacion() + " no encontrada"));
            venta.setCotizacion(cotizacion);
        }

        if (venta.getTotal() == null) {
            venta.setTotal(BigDecimal.ZERO);
        }

        // Establecer la relación bidireccional con los detalles
        if (venta.getDetalles() != null) {
            venta.getDetalles().forEach(detalle -> {
                detalle.setVenta(venta);
                if (detalle.getDescripcionProducto() == null && detalle.getProducto() != null) {
                    detalle.setDescripcionProducto(detalle.getProducto().getDescripcion());
                }
            });
        }

        return ventaRepository.save(venta);
    }

    public Venta actualizar(Integer idVenta, Venta ventaActualizada) {
        Venta ventaExistente = buscarPorId(idVenta);

        ventaExistente.setFecha(ventaActualizada.getFecha());
        ventaExistente.setTotal(ventaActualizada.getTotal());
        ventaExistente.setNombre(ventaActualizada.getNombre());
        ventaExistente.setTipoPago(ventaActualizada.getTipoPago());

        // NUEVO: Validar cotización si se actualiza
        if (ventaActualizada.getCotizacion() != null && ventaActualizada.getCotizacion().getIdCotizacion() != null) {
            Cotizacion cotizacion = cotizacionRepository.findById(ventaActualizada.getCotizacion().getIdCotizacion())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cotización con ID " + ventaActualizada.getCotizacion().getIdCotizacion() + " no encontrada"));
            ventaExistente.setCotizacion(cotizacion);
        }

        if (ventaActualizada.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(ventaActualizada.getCliente().getCedula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente con cédula " + ventaActualizada.getCliente().getCedula() + " no encontrado"));
            ventaExistente.setCliente(cliente);
        }

        // Actualizar detalles si vienen en la petición
        if (ventaActualizada.getDetalles() != null) {
            ventaExistente.getDetalles().clear();

            ventaActualizada.getDetalles().forEach(detalle -> {
                detalle.setVenta(ventaExistente);
                if (detalle.getDescripcionProducto() == null && detalle.getProducto() != null) {
                    detalle.setDescripcionProducto(detalle.getProducto().getDescripcion());
                }
                ventaExistente.getDetalles().add(detalle);
            });
        }

        return ventaRepository.save(ventaExistente);
    }

    public void eliminar(Integer idVenta) {
        Venta venta = buscarPorId(idVenta);
        ventaRepository.delete(venta);
    }
}