package com.ferregestion.service;

import com.ferregestion.entity.Producto;
import com.ferregestion.exception.DuplicateResourceException;
import com.ferregestion.exception.ResourceNotFoundException;
import com.ferregestion.repository.ProductoRepository;
import org.springframework.stereotype.Service;

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
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new DuplicateResourceException("Ya existe un producto con el nombre " + producto.getNombre());
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto actualizado) {
        Producto p = buscarPorId(id);
        p.setNombre(actualizado.getNombre());
        p.setDescripcion(actualizado.getDescripcion());
        p.setPrecio(actualizado.getPrecio());
        p.setStock(actualizado.getStock());
        return productoRepository.save(p);
    }

    public void eliminar(Integer id) {
        Producto p = buscarPorId(id);
        productoRepository.delete(p);
    }

    /**
     * Reduce el stock del producto en la cantidad indicada. Lanza InsufficientStockException si no alcanza.
     */
    public void reducirStock(Integer productoId, int cantidad) {
        Producto p = buscarPorId(productoId);
        if (p.getStock() < cantidad) {
            throw new com.ferregestion.exception.InsufficientStockException(
                    "Stock insuficiente para el producto " + p.getNombre() + ". Disponible: " + p.getStock());
        }
        p.setStock(p.getStock() - cantidad);
        productoRepository.save(p);
    }

    /**
     * Aumenta stock (útil para reversar ventas).
     */
    public void aumentarStock(Integer productoId, int cantidad) {
        Producto p = buscarPorId(productoId);
        p.setStock(p.getStock() + cantidad);
        productoRepository.save(p);
    }
}
