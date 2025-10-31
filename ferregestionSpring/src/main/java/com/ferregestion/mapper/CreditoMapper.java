package com.ferregestion.mapper;

import com.ferregestion.dto.request.CreditoRequestDTO;
import com.ferregestion.dto.response.CreditoResponseDTO;
import com.ferregestion.entity.Cliente;
import com.ferregestion.entity.Credito;
import com.ferregestion.entity.Venta;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ClienteRepository;
import com.ferregestion.repository.VentaRepository;
import org.springframework.stereotype.Component;

@Component
public class CreditoMapper {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public CreditoMapper(VentaRepository ventaRepository,
                         ClienteRepository clienteRepository,
                         ClienteMapper clienteMapper) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public Credito toEntity(CreditoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Venta venta = ventaRepository.findById(dto.getIdVenta())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venta con ID " + dto.getIdVenta() + " no encontrada"));

        Cliente cliente = clienteRepository.findById(dto.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con cédula " + dto.getCedula() + " no encontrado"));

        return Credito.builder()
                .venta(venta)
                .cliente(cliente)
                .nombre(dto.getNombre())
                .montoTotal(dto.getMontoTotal())
                .saldoPendiente(dto.getSaldoPendiente())
                .estado(dto.getEstado())
                .build();
    }

    public CreditoResponseDTO toResponseDTO(Credito entity) {
        if (entity == null) {
            return null;
        }

        System.out.println("Mapeando crédito: " + entity.getIdCredito());

        return CreditoResponseDTO.builder()
                .idCredito(entity.getIdCredito())
                .idVenta(entity.getVenta() != null ? entity.getVenta().getIdVenta() : null)
                .idCliente(entity.getCliente() != null ? entity.getCliente().getCedula() : null)
                .nombreCliente(entity.getCliente() != null ? entity.getCliente().getNombre() : null)
                .nombre(entity.getNombre())
                .montoTotal(entity.getMontoTotal())
                .saldoPendiente(entity.getSaldoPendiente())
                .estado(entity.getEstado())
                .build();
    }

    public void updateEntityFromDTO(CreditoRequestDTO dto, Credito entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
        entity.setMontoTotal(dto.getMontoTotal());
        entity.setSaldoPendiente(dto.getSaldoPendiente());
        entity.setEstado(dto.getEstado());

        if (dto.getIdVenta() != null) {
            Venta venta = ventaRepository.findById(dto.getIdVenta())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Venta con ID " + dto.getIdVenta() + " no encontrada"));
            entity.setVenta(venta);
        }

        if (dto.getCedula() != null) {
            Cliente cliente = clienteRepository.findById(dto.getCedula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente con cédula " + dto.getCedula() + " no encontrado"));
            entity.setCliente(cliente);
        }
    }
}