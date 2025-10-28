package com.ferregestion.service;

import com.ferregestion.entity.Producto;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
    }

    public Producto guardar(Producto producto) {
        if (producto.getIva() == null) {
            producto.setIva(new BigDecimal("19.00"));
        }
        if (producto.getPrecioCompra() == null) {
            producto.setPrecioCompra(BigDecimal.ZERO);
        }
        if (producto.getPrecioVenta() == null) {
            producto.setPrecioVenta(BigDecimal.ZERO);
        }
        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto actualizado) {
        Producto p = buscarPorId(id);
        p.setDescripcion(actualizado.getDescripcion());
        p.setClase(actualizado.getClase());
        p.setGrupo(actualizado.getGrupo());
        p.setIva(actualizado.getIva());
        p.setPrecioCompra(actualizado.getPrecioCompra());
        p.setPrecioVenta(actualizado.getPrecioVenta());
        p.setStock(actualizado.getStock());
        return productoRepository.save(p);
    }

    public void eliminar(Integer id) {
        Producto p = buscarPorId(id);
        productoRepository.delete(p);
    }

    public void reducirStock(Integer productoId, int cantidad) {
        Producto p = buscarPorId(productoId);
        if (p.getStock() < cantidad) {
            throw new com.ferregestion.exception.InsufficientStockException(
                    "Stock insuficiente para el producto " + p.getDescripcion() + ". Disponible: " + p.getStock());
        }
        p.setStock(p.getStock() - cantidad);
        productoRepository.save(p);
    }

    public void aumentarStock(Integer productoId, int cantidad) {
        Producto p = buscarPorId(productoId);
        p.setStock(p.getStock() + cantidad);
        productoRepository.save(p);
    }
}