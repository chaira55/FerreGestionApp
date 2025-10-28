package com.ferregestion.mapper;

import com.ferregestion.dto.request.DetalleVentaRequestDTO;
import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.response.DetalleVentaResponseDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.entity.*;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ClienteRepository;
import com.ferregestion.repository.CotizacionRepository;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VentaMapper {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CotizacionRepository cotizacionRepository;
    private final ClienteMapper clienteMapper;

    public VentaMapper(ClienteRepository clienteRepository,
                       ProductoRepository productoRepository,
                       CotizacionRepository cotizacionRepository,
                       ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.clienteMapper = clienteMapper;
    }

    public Venta toEntity(VentaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        // Buscar cliente
        Cliente cliente = clienteRepository.findById(dto.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con cédula " + dto.getCedula() + " no encontrado"));

        Venta venta = Venta.builder()
                .cliente(cliente)
                .nombre(dto.getNombre())
                .fecha(dto.getFecha())
                .total(dto.getTotal())
                .tipoPago(dto.getTipoPago())
                .detalles(new ArrayList<>())
                .build();

        // Buscar cotización si existe
        if (dto.getIdCotizacion() != null) {
            Cotizacion cotizacion = cotizacionRepository.findById(dto.getIdCotizacion())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cotización con ID " + dto.getIdCotizacion() + " no encontrada"));
            venta.setCotizacion(cotizacion);
        }

        // Mapear detalles
        if (dto.getDetalles() != null) {
            List<DetalleVenta> detalles = dto.getDetalles().stream()
                    .map(detalleDTO -> toDetalleEntity(detalleDTO, venta))
                    .collect(Collectors.toList());
            venta.setDetalles(detalles);
        }

        return venta;
    }

    private DetalleVenta toDetalleEntity(DetalleVentaRequestDTO dto, Venta venta) {
        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto con ID " + dto.getIdProducto() + " no encontrado"));

        return DetalleVenta.builder()
                .venta(venta)
                .producto(producto)
                .descripcionProducto(dto.getDescripcionProducto() != null ?
                        dto.getDescripcionProducto() : producto.getDescripcion())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .subtotal(dto.getSubtotal())
                .build();
    }

    public VentaResponseDTO toResponseDTO(Venta entity) {
        if (entity == null) {
            return null;
        }

        return VentaResponseDTO.builder()
                .idVenta(entity.getIdVenta())
                .cliente(clienteMapper.toResponseDTO(entity.getCliente()))
                .nombre(entity.getNombre())
                .idCotizacion(entity.getCotizacion() != null ?
                        entity.getCotizacion().getIdCotizacion() : null)
                .fecha(entity.getFecha())
                .total(entity.getTotal())
                .tipoPago(entity.getTipoPago())
                .detalles(entity.getDetalles() != null ?
                        entity.getDetalles().stream()
                                .map(this::toDetalleResponseDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    private DetalleVentaResponseDTO toDetalleResponseDTO(DetalleVenta entity) {
        return DetalleVentaResponseDTO.builder()
                .idDetalleVenta(entity.getIdDetalleVenta())
                .idProducto(entity.getProducto().getIdProducto())
                .descripcionProducto(entity.getDescripcionProducto())
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(entity.getSubtotal())
                .build();
    }
}