package com.ferregestion.service;

import com.ferregestion.dto.request.ProductoRequestDTO;
import com.ferregestion.dto.response.ProductoResponseDTO;
import com.ferregestion.entity.Producto;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.mapper.ProductoMapper;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    // Metodo sin paginación
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Metodo con paginación
    public Page<ProductoResponseDTO> listarTodosPaginado(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return productoRepository.findAll(pageable)
                .map(productoMapper::toResponseDTO);
    }

    // Buscar por descripción (con paginación)
    public Page<ProductoResponseDTO> buscarPorDescripcion(String descripcion, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findByDescripcionContainingIgnoreCase(descripcion, pageable)
                .map(productoMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por clase
    public Page<ProductoResponseDTO> filtrarPorClase(String codigoClase, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findByClaseCodigoClase(codigoClase, pageable)
                .map(productoMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por grupo
    public Page<ProductoResponseDTO> filtrarPorGrupo(String codigoGrupo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findByGrupoCodigoGrupo(codigoGrupo, pageable)
                .map(productoMapper::toResponseDTO);
    }

    // NUEVO: Filtrar por rango de precio
    public Page<ProductoResponseDTO> filtrarPorRangoPrecio(
            BigDecimal precioMin,
            BigDecimal precioMax,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findByPrecioVentaBetween(precioMin, precioMax, pageable)
                .map(productoMapper::toResponseDTO);
    }

    // NUEVO: Productos con stock bajo
    public List<ProductoResponseDTO> listarStockBajo(int stockMinimo) {
        return productoRepository.findByStockLessThanEqual(stockMinimo).stream()
                .map(productoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductoResponseDTO buscarPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
        return productoMapper.toResponseDTO(producto);
    }

    public ProductoResponseDTO guardar(ProductoRequestDTO productoDTO) {
        Producto producto = productoMapper.toEntity(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return productoMapper.toResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO actualizar(Integer id, ProductoRequestDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));

        productoMapper.updateEntityFromDTO(productoDTO, productoExistente);
        Producto productoActualizado = productoRepository.save(productoExistente);
        return productoMapper.toResponseDTO(productoActualizado);
    }

    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
        productoRepository.delete(producto);
    }

    public void reducirStock(Integer productoId, int cantidad) {
        Producto p = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + productoId + " no encontrado"));

        if (p.getStock() < cantidad) {
            throw new com.ferregestion.exception.InsufficientStockException(
                    "Stock insuficiente para el producto " + p.getDescripcion() + ". Disponible: " + p.getStock());
        }
        p.setStock(p.getStock() - cantidad);
        productoRepository.save(p);
    }

    public void aumentarStock(Integer productoId, int cantidad) {
        Producto p = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + productoId + " no encontrado"));
        p.setStock(p.getStock() + cantidad);
        productoRepository.save(p);
    }
}