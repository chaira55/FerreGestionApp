package com.ferregestion.service;

import com.ferregestion.entity.Credito;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.CreditoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditoService {

    private final CreditoRepository creditoRepository;

    public CreditoService(CreditoRepository creditoRepository) {
        this.creditoRepository = creditoRepository;
    }

    public List<Credito> listarTodos() {
        return creditoRepository.findAll();
    }

    public Credito buscarPorId(Integer idCredito) {
        return creditoRepository.findById(idCredito)
                .orElseThrow(() -> new ResourceNotFoundException("Crédito con ID " + idCredito + " no encontrado"));
    }

    public Credito guardar(Credito credito) {
        return creditoRepository.save(credito);
    }

    public Credito actualizar(Integer idCredito, Credito creditoActualizado) {
        Credito creditoExistente = buscarPorId(idCredito);
        creditoExistente.setCliente(creditoActualizado.getCliente());
        creditoExistente.setValor(creditoActualizado.getValor());
        creditoExistente.setFechaInicio(creditoActualizado.getFechaInicio());
        creditoExistente.setFechaVencimiento(creditoActualizado.getFechaVencimiento());
        creditoExistente.setEstado(creditoActualizado.getEstado());
        return creditoRepository.save(creditoExistente);
    }

    public void eliminar(Integer idCredito) {
        Credito credito = buscarPorId(idCredito);
        creditoRepository.delete(credito);
    }
}
