package com.ferregestion.mapper;

import com.ferregestion.dto.request.CotizacionRequestDTO;
import com.ferregestion.dto.request.DetalleCotizacionRequestDTO;
import com.ferregestion.dto.response.CotizacionResponseDTO;
import com.ferregestion.dto.response.DetalleCotizacionResponseDTO;
import com.ferregestion.entity.Cliente;
import com.ferregestion.entity.Cotizacion;
import com.ferregestion.entity.DetalleCotizacion;
import com.ferregestion.entity.Producto;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ClienteRepository;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CotizacionMapper {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ClienteMapper clienteMapper;

    public CotizacionMapper(ClienteRepository clienteRepository,
                            ProductoRepository productoRepository,
                            ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.clienteMapper = clienteMapper;
    }

    public Cotizacion toEntity(CotizacionRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        // Buscar cliente
        Cliente cliente = clienteRepository.findById(dto.getCedula())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con cédula " + dto.getCedula() + " no encontrado"));

        Cotizacion cotizacion = Cotizacion.builder()
                .cliente(cliente)
                .nombre(dto.getNombre())
                .fecha(dto.getFecha())
                .total(dto.getTotal())
                .detalles(new ArrayList<>())
                .build();

        // Mapear detalles
        if (dto.getDetalles() != null) {
            List<DetalleCotizacion> detalles = dto.getDetalles().stream()
                    .map(detalleDTO -> toDetalleEntity(detalleDTO, cotizacion))
                    .collect(Collectors.toList());
            cotizacion.setDetalles(detalles);
        }

        return cotizacion;
    }

    private DetalleCotizacion toDetalleEntity(DetalleCotizacionRequestDTO dto, Cotizacion cotizacion) {
        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto con ID " + dto.getIdProducto() + " no encontrado"));

        return DetalleCotizacion.builder()
                .cotizacion(cotizacion)
                .producto(producto)
                .descripcionProducto(dto.getDescripcionProducto() != null ?
                        dto.getDescripcionProducto() : producto.getDescripcion())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .subtotalProducto(dto.getSubtotalProducto())
                .build();
    }

    public CotizacionResponseDTO toResponseDTO(Cotizacion entity) {
        if (entity == null) {
            return null;
        }

        return CotizacionResponseDTO.builder()
                .idCotizacion(entity.getIdCotizacion())
                .cliente(clienteMapper.toResponseDTO(entity.getCliente()))
                .nombre(entity.getNombre())
                .fecha(entity.getFecha())
                .total(entity.getTotal())
                .detalles(entity.getDetalles() != null ?
                        entity.getDetalles().stream()
                                .map(this::toDetalleResponseDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    private DetalleCotizacionResponseDTO toDetalleResponseDTO(DetalleCotizacion entity) {
        return DetalleCotizacionResponseDTO.builder()
                .idDetalleCotizacion(entity.getIdDetalleCotizacion())
                .idProducto(entity.getProducto().getIdProducto())
                .descripcionProducto(entity.getDescripcionProducto())
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotalProducto(entity.getSubtotalProducto())
                .build();
    }
}