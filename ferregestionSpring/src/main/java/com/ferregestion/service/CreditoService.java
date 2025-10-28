package com.ferregestion.service;

import com.ferregestion.entity.Credito;
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

    public Credito buscarPorId(Integer id) {
        return creditoRepository.findById(id).orElse(null);
    }

    public Credito guardar(Credito credito) {
        return creditoRepository.save(credito);
    }

    public Credito actualizar(Integer id, Credito creditoActualizado) {
        return creditoRepository.findById(id)
                .map(credito -> {
                    credito.setVenta(creditoActualizado.getVenta());
                    credito.setCliente(creditoActualizado.getCliente());
                    credito.setNombre(creditoActualizado.getNombre());
                    credito.setMontoTotal(creditoActualizado.getMontoTotal());
                    credito.setSaldoPendiente(creditoActualizado.getSaldoPendiente());
                    credito.setEstado(creditoActualizado.getEstado());
                    return creditoRepository.save(credito);
                })
                .orElse(null);
    }

    public void eliminar(Integer id) {
        creditoRepository.deleteById(id);
    }
}