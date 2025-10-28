package com.ferregestion.mapper;

import com.ferregestion.dto.request.PagoRequestDTO;
import com.ferregestion.dto.response.PagoResponseDTO;
import com.ferregestion.entity.Cliente;
import com.ferregestion.entity.Credito;
import com.ferregestion.entity.Pago;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ClienteRepository;
import com.ferregestion.repository.CreditoRepository;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    private final CreditoRepository creditoRepository;
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public PagoMapper(CreditoRepository creditoRepository,
                      ClienteRepository clienteRepository,
                      ClienteMapper clienteMapper) {
        this.creditoRepository = creditoRepository;
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public Pago toEntity(PagoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Credito credito = creditoRepository.findById(dto.getIdCredito())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Crédito con ID " + dto.getIdCredito() + " no encontrado"));

        Cliente cliente = clienteRepository.findById(dto.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con cédula " + dto.getCedula() + " no encontrado"));

        return Pago.builder()
                .credito(credito)
                .cliente(cliente)
                .nombre(dto.getNombre())
                .fechaPago(dto.getFechaPago())
                .monto(dto.getMonto())
                .build();
    }

    public PagoResponseDTO toResponseDTO(Pago entity) {
        if (entity == null) {
            return null;
        }

        return PagoResponseDTO.builder()
                .idPago(entity.getIdPago())
                .idCredito(entity.getCredito().getIdCredito())
                .cliente(clienteMapper.toResponseDTO(entity.getCliente()))
                .nombre(entity.getNombre())
                .fechaPago(entity.getFechaPago())
                .monto(entity.getMonto())
                .build();
    }

    public void updateEntityFromDTO(PagoRequestDTO dto, Pago entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
        entity.setFechaPago(dto.getFechaPago());
        entity.setMonto(dto.getMonto());

        if (dto.getIdCredito() != null) {
            Credito credito = creditoRepository.findById(dto.getIdCredito())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Crédito con ID " + dto.getIdCredito() + " no encontrado"));
            entity.setCredito(credito);
        }

        if (dto.getCedula() != null) {
            Cliente cliente = clienteRepository.findById(dto.getCedula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente con cédula " + dto.getCedula() + " no encontrado"));
            entity.setCliente(cliente);
        }
    }
}