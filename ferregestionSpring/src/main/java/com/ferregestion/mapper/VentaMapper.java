package com.ferregestion.mapper;

import com.ferregestion.dto.request.DetalleVentaRequestDTO;
import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.response.DetalleVentaResponseDTO;
import com.ferregestion.dto.response.ProductoResponseDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.entity.*;
import com.ferregestion.repository.ClienteRepository;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VentaMapper {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ClienteMapper clienteMapper;
    private final ProductoMapper productoMapper;

    public VentaMapper(ClienteRepository clienteRepository,
                       ProductoRepository productoRepository,
                       ClienteMapper clienteMapper,
                       ProductoMapper productoMapper) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.clienteMapper = clienteMapper;
        this.productoMapper = productoMapper;
    }

    // Convertir VentaRequestDTO a Venta (Entity)
    public Venta toEntity(VentaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Venta venta = Venta.builder()
                .fecha(dto.getFecha())
                .tipoPago(dto.getTipoPago())
                .total(dto.getTotal())
                .build();

        // Cargar cliente
        if (dto.getCedula() != null) {
            Cliente cliente = clienteRepository.findById(dto.getCedula())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con cédula: " + dto.getCedula()));
            venta.setCliente(cliente);
        }

        // Cargar detalles
        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            List<DetalleVenta> detalles = new ArrayList<>();

            for (DetalleVentaRequestDTO detalleDTO : dto.getDetalles()) {
                // Calcular subtotal
                BigDecimal subtotal = detalleDTO.getPrecioUnitario()
                        .multiply(new BigDecimal(detalleDTO.getCantidad()));

                DetalleVenta detalle = DetalleVenta.builder()
                        .cantidad(detalleDTO.getCantidad())
                        .precioUnitario(detalleDTO.getPrecioUnitario())
                        .subtotal(subtotal)
                        .venta(venta)
                        .build();

                // Cargar producto
                Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + detalleDTO.getIdProducto()));
                detalle.setProducto(producto);

                detalles.add(detalle);
            }

            venta.setDetalles(detalles);
        }

        return venta;
    }

    // Convertir Venta (Entity) a VentaResponseDTO
    public VentaResponseDTO toResponseDTO(Venta entity) {
        if (entity == null) {
            return null;
        }

        return VentaResponseDTO.builder()
                .idVenta(entity.getIdVenta())
                .cliente(clienteMapper.toResponseDTO(entity.getCliente()))
                .fecha(entity.getFecha())
                .total(entity.getTotal())
                .tipoPago(entity.getTipoPago())
                .detalles(entity.getDetalles() != null ?
                        entity.getDetalles().stream()
                                .map(this::toDetalleResponseDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    // Convertir DetalleVenta (Entity) a DetalleVentaResponseDTO
    private DetalleVentaResponseDTO toDetalleResponseDTO(DetalleVenta entity) {
        if (entity == null) {
            return null;
        }

        BigDecimal subtotal = entity.getSubtotal();
        if (subtotal == null && entity.getCantidad() != null && entity.getPrecioUnitario() != null) {
            subtotal = entity.getPrecioUnitario().multiply(new BigDecimal(entity.getCantidad()));
        }

        // Crear ProductoResponseDTO manualmente (sin mapper para evitar dependencia circular)
        ProductoResponseDTO productoDTO = null;
        if (entity.getProducto() != null) {
            Producto p = entity.getProducto();
            productoDTO = ProductoResponseDTO.builder()
                    .idProducto(p.getIdProducto())
                    .descripcion(p.getDescripcion())
                    .precioVenta(p.getPrecioVenta())
                    .stock(p.getStock())
                    .iva(p.getIva())
                    .build();
        }

        return DetalleVentaResponseDTO.builder()
                .idDetalleVenta(entity.getIdDetalleVenta())  // ✅ CORREGIDO
                .producto(productoDTO)
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(subtotal)
                .build();
    }
}