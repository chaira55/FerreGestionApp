package com.ferregestion.service;

import com.ferregestion.entity.Cotizacion;
import com.ferregestion.entity.Cliente;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.CotizacionRepository;
import com.ferregestion.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final ClienteRepository clienteRepository;

    public CotizacionService(CotizacionRepository cotizacionRepository, ClienteRepository clienteRepository) {
        this.cotizacionRepository = cotizacionRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Cotizacion> listarTodas() {
        return cotizacionRepository.findAll();
    }

    public Cotizacion buscarPorId(Integer id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotización con ID " + id + " no encontrada"));
    }

    public Cotizacion guardar(Cotizacion cotizacion) {
        // Validar cliente existente
        Cliente cliente = clienteRepository.findById(cotizacion.getCliente().getCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con cédula " + cotizacion.getCliente().getCedula() + " no encontrado"));

        cotizacion.setCliente(cliente);

        return cotizacionRepository.save(cotizacion);
    }

    public Cotizacion actualizar(Integer id, Cotizacion cotizacionActualizada) {
        Cotizacion cotizacionExistente = buscarPorId(id);

        cotizacionExistente.setFecha(cotizacionActualizada.getFecha());
        cotizacionExistente.setTotal(cotizacionActualizada.getTotal());

        // Validar cliente actualizado si cambia
        if (cotizacionActualizada.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(cotizacionActualizada.getCliente().getCedula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente con cédula " + cotizacionActualizada.getCliente().getCedula() + " no encontrado"));
            cotizacionExistente.setCliente(cliente);
        }

        return cotizacionRepository.save(cotizacionExistente);
    }

    public void eliminar(Integer id) {
        Cotizacion cotizacion = buscarPorId(id);
        cotizacionRepository.delete(cotizacion);
    }
}
