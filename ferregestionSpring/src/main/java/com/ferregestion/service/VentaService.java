package com.ferregestion.service;

import com.ferregestion.entity.Venta;
import com.ferregestion.entity.Cliente;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.VentaRepository;
import com.ferregestion.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(VentaRepository ventaRepository, ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
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

        // Si quieres, aquí se puede calcular el total automáticamente desde los detalles
        // venta.setTotal(calcularTotal(venta.getDetalles()));

        return ventaRepository.save(venta);
    }

    public Venta actualizar(Integer idVenta, Venta ventaActualizada) {
        Venta ventaExistente = buscarPorId(idVenta);

        ventaExistente.setFecha(ventaActualizada.getFecha());
        ventaExistente.setTotal(ventaActualizada.getTotal());
        ventaExistente.setNombre(ventaActualizada.getNombre());  // NUEVO
        ventaExistente.setTipoPago(ventaActualizada.getTipoPago());  // NUEVO
        ventaExistente.setCotizacion(ventaActualizada.getCotizacion());  // NUEVO

        if (ventaActualizada.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(ventaActualizada.getCliente().getCedula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente con cédula " + ventaActualizada.getCliente().getCedula() + " no encontrado"));
            ventaExistente.setCliente(cliente);
        }

        return ventaRepository.save(ventaExistente);
    }

    public void eliminar(Integer idVenta) {
        Venta venta = buscarPorId(idVenta);
        ventaRepository.delete(venta);
    }
}
