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

    public Pago buscarPorId(Integer idPago) {
        return pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + idPago + " no encontrado"));
    }

    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago actualizar(Integer idPago, Pago pagoActualizado) {
        Pago pagoExistente = buscarPorId(idPago);
        pagoExistente.setCliente(pagoActualizado.getCliente());
        pagoExistente.setCredito(pagoActualizado.getCredito());
        pagoExistente.setValor(pagoActualizado.getValor());
        pagoExistente.setFecha(pagoActualizado.getFecha());
        pagoExistente.setEstado(pagoActualizado.getEstado());
        return pagoRepository.save(pagoExistente);
    }

    public void eliminar(Integer idPago) {
        Pago pago = buscarPorId(idPago);
        pagoRepository.delete(pago);
    }
}
