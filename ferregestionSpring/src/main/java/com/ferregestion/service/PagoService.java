package com.ferregestion.service;

import com.ferregestion.entity.Pago;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public Pago buscarPorId(Integer id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + id + " no encontrado"));
    }

    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago actualizar(Integer id, Pago pagoActualizado) {
        Pago pagoExistente = buscarPorId(id);

        pagoExistente.setCredito(pagoActualizado.getCredito());
        pagoExistente.setCliente(pagoActualizado.getCliente());
        pagoExistente.setNombre(pagoActualizado.getNombre());
        pagoExistente.setFechaPago(pagoActualizado.getFechaPago());
        pagoExistente.setMonto(pagoActualizado.getMonto());  // CAMBIO: getValor() → getMonto()

        return pagoRepository.save(pagoExistente);
    }

    public void eliminar(Integer id) {
        Pago pago = buscarPorId(id);
        pagoRepository.delete(pago);
    }
}