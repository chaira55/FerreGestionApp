package com.ferregestion.service;

import com.ferregestion.dto.request.VentaRequestDTO;
import com.ferregestion.dto.response.VentaResponseDTO;
import com.ferregestion.entity.Venta;
import com.ferregestion.entity.DetalleVenta;
import com.ferregestion.entity.Producto;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.VentaMapper;
import com.ferregestion.repository.VentaRepository;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository,
                        VentaMapper ventaMapper,
                        ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public VentaResponseDTO guardar(VentaRequestDTO ventaDTO) {
        // Convertir DTO a entidad
        Venta venta = ventaMapper.toEntity(ventaDTO);

        // DESCONTAR STOCK DE CADA PRODUCTO
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();

            if (producto == null) {
                throw new RuntimeException("Producto no encontrado en el detalle");
            }

            // Verificar que hay stock suficiente
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para " + producto.getDescripcion() +
                                ". Disponible: " + producto.getStock() +
                                ", Solicitado: " + detalle.getCantidad()
                );
            }

            // Descontar stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }

        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(venta);

        return ventaMapper.toResponseDTO(ventaGuardada);
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarTodos() {
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO buscarPorId(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
        return ventaMapper.toResponseDTO(venta);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Venta no encontrada con id: " + id);
        }

        // DEVOLVER EL STOCK AL ELIMINAR UNA VENTA
        Venta venta = ventaRepository.findById(id).orElseThrow();

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            if (producto != null) {
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productoRepository.save(producto);
            }
        }

        ventaRepository.deleteById(id);
    }
}